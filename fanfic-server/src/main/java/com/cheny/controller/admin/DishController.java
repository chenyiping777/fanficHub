package com.cheny.controller.admin;


import com.cheny.dto.DishDto;
import com.cheny.entity.Dish;
import com.cheny.entity.Result;
import com.cheny.query.DishQuery;
import com.cheny.service.DishService;
import com.cheny.vo.DishVo;
import com.cheny.vo.PageVo;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 精确删除缓存中的单个菜品字段（Hash field: dishId）
     * 只移除该菜品，同分类下其他菜品的缓存不受影响
     * 若 Hash 删除后为空，则清理整个 key 和空值缓存标记
     */
    private void evictDishField(Long categoryId, Long dishId) {
        if (categoryId == null || dishId == null) return;
        String key = "dish:" + categoryId;
        redisTemplate.opsForHash().delete(key, dishId.toString());
        // opsForHash()这个方法代表对Hash类型的数据进行操作
        // 1.delete()这个方法代表删除Hash类型数据中的指定字段
        // 2.get(key,field) 这个方法代表获取Hash类型数据中指定字段（value）的值
        // 3.size(key) 这个方法代表获取Hash类型数据中字段的数量
        // 4.keys(key) 返回这个key对应的field集合
        // 5.values(key) 返回这个key对应的value集合
        // 6.entries(key) 返回这个key对应的field和value的映射关系集合
        // Hash 已无字段，清理 key 和空值标记
        if (redisTemplate.opsForHash().size(key) == 0) {
            redisTemplate.delete(key);
            redisTemplate.delete("dish:empty:" + categoryId);
        }
    }

    //查询单条,回显
    @GetMapping("/{id}")
    public Result getDishById(@PathVariable Long id){
        log.info("要查询的id为: {}",id );
        DishVo dishVo = dishService.getDishById(id);
        //为什么这里不去缓存里查，因为缓存里没有这个数据，缓存里只有分类id对应的菜品列表
        return Result.success(dishVo);
    }

    //删除单条--通过点击某一条记录的删除按钮传送过来的id
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Long id){
        log.info("要删除的id为: {}",id );
        // 先查出categoryId，用于定位缓存中的Hash key
        Dish dish = dishService.getById(id);
        Long categoryId = (dish != null) ? dish.getCategoryId() : null;
        // 先更新数据库
        dishService.removeDishById(id);
        // 再精确删除该菜品在缓存中的field，不影响同分类其他菜品
        evictDishField(categoryId, id);
        return Result.success();
    }

    //删除多条
    @DeleteMapping("/{ids}")
    public Result deleteIds(@PathVariable List<Long> ids){
        log.info("要删除的ids为: {}",ids );
        // 先批量查出每个菜品对应的categoryId，用于精确删除缓存field

        //dishId，categoryId
        Map<Long, Long> dishCategoryMap = ids.stream()
                .map(id -> dishService.getById(id))//这个map返回什么：Dish对象
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Dish::getId, Dish::getCategoryId));
        //删数据库，再删缓存
        dishService.removeBatchByIds(ids);
        // 再逐个精确删除缓存field，不影响同分类下其他菜品
        dishCategoryMap.forEach((dishId, categoryId) -> evictDishField(categoryId, dishId));
        return Result.success();
    }

    //起售停售状态修改
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status,@RequestParam Long id){
        log.info("要禁用/启用的菜品id为: {}",id );
        // 先查出categoryId
        Dish dish = dishService.getById(id);
        Long categoryId = (dish != null) ? dish.getCategoryId() : null;
        // 先更新数据库
        dishService.updateStatus(status,id);
        // 再精确删除该菜品的缓存field（下次查询会从DB重新加载最新状态）
        evictDishField(categoryId, id);
        return Result.success();
    }

    //分页查询
    @GetMapping("/page")
    public Result page(@RequestBody DishQuery dishQuery){
        PageVo<DishVo> pageVo = dishService.getDishPage(dishQuery);
        return Result.success(pageVo);
    }

    //根据id修改
    @PutMapping
    public Result update(@ModelAttribute @Valid DishDto dishDto,
                         @RequestParam(required = false) MultipartFile imageFile){
        //主键，名称，价格，分类，状态，图片都不能为空 描述可以
        log.info("修改后的菜品为为: {}",dishDto);
        // 先更新数据库
        dishService.updateDish(dishDto,imageFile);
        // 再精确删除该菜品的缓存field（下次查询从DB加载最新数据）
        evictDishField(dishDto.getCategoryId(), dishDto.getId());
        return Result.success();
    }

    @PostMapping
    public Result add(@ModelAttribute @Valid DishDto dishDto,
                       @RequestParam(required = false) MultipartFile imageFile){
        //名称，价格，分类，状态,图片都不能为空 描述可以
        log.info("要添加的菜品为: {}",dishDto);
        // 先更新数据库
        dishService.addDish(dishDto,imageFile);
        // 新增菜品：只需清理该分类的空值缓存标记
        // 下次该分类全量查询时会从DB重建完整缓存（包含新菜品）
        if (dishDto.getCategoryId() != null) {
            redisTemplate.delete("dish:empty:" + dishDto.getCategoryId());
            /*如果这个 key 根本不存在，DEL 不会报错、不会抛异常，只是返回删除数量 0，程序正常往下跑。*/
        }
        /*dish:empty:{categoryId}的含义是「该分类当前没有菜品」。
当业务状态发生翻转：分类从没有菜品 → 新增了菜品，这个旧的 empty 标记就失效了，必须清理。*/
        return Result.success();
    }

    @GetMapping("/list")//根据分类id查询菜品
    public Result list(Integer categoryId) {
        List<DishVo> dishVos = dishService.getDishesById(categoryId);
        return Result.success(dishVos);
    }

}
