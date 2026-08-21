package com.cheny.controller.admin;


import com.cheny.dto.SetmealDto;
import com.cheny.entity.Result;
import com.cheny.query.SetmealQuery;
import com.cheny.service.SetmealService;
import com.cheny.vo.PageVo;
import com.cheny.vo.SetmealVo;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/setMeal")
public class SetMealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据套餐id删除缓存（String key: setmeal:{id}）
     * 同时清理空值缓存（setmeal:empty:{id}）
     */
    private void cleanSetmealCache(Long id) {
        if (id != null) {
            redisTemplate.delete("setmeal:" + id);
            redisTemplate.delete("setmeal:empty:" + id);
        }
    }

    //起售停售状态修改
    @PostMapping("/status/{status}")//put做全量更新
    public Result startOrStop(@PathVariable Integer status, @RequestParam Long id){
        log.info("要禁用/启用的分类id为: {}",id );
        // 先更新数据库
        setmealService.updateStatus(status,id);
        // 再删除缓存
        cleanSetmealCache(id);
        return Result.success();
    }

    //删除，批量删除
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Long id){
        log.info("要删除的id为: {}",id );
        // 先更新数据库
        setmealService.removeSetmealById(id);
        // 再删除缓存
        cleanSetmealCache(id);
        return Result.success();
    }

    @DeleteMapping("/batch")
    public Result deleteByIds(@RequestParam List<Long> ids){
        log.info("要删除的id集合为: {}",ids );
        // 先更新数据库
        setmealService.removeSetmealByIds(ids);
        // 再逐个删除缓存
        ids.forEach(this::cleanSetmealCache);
        return Result.success();
    }

    //按照套餐名称，套餐分类，售卖状态分页查询
    @GetMapping("/page")
    public Result page(@RequestBody SetmealQuery setMealQuery){
        log.info("分页查询的对象：{}",setMealQuery);
        PageVo<SetmealVo> pageVo = setmealService.pageSetmeal(setMealQuery);
        return Result.success(pageVo);

    }


    //新建套餐
    @PostMapping
    public Result add(@ModelAttribute @Valid SetmealDto setmealDto,
                      @RequestParam(required = false) MultipartFile imageFile){
        log.info("要添加的套餐为: {}",setmealDto);
        // 新增操作，无需删缓存（该套餐尚无缓存）
        setmealService.addSetmeal(setmealDto,imageFile);
        return Result.success();

    }

    //修改
    @PutMapping
    public Result update(@ModelAttribute @Valid SetmealDto setmealDto,
                      @RequestParam(required = false) MultipartFile imageFile){
        log.info("要修改的套餐为: {}",setmealDto);
        // 先更新数据库
        setmealService.updateSetmeal(setmealDto,imageFile);
        // 再删除缓存
        cleanSetmealCache(setmealDto.getId());
        return Result.success();

    }


}
