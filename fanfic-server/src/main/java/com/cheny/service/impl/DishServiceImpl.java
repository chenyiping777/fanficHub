package com.cheny.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.cheny.constant.MessageConstant;
import com.cheny.constant.StatusConstant;
import com.cheny.dto.DishDto;
import com.cheny.entity.Dish;
import com.cheny.entity.DishFlavor;
import com.cheny.entity.SetmealDish;
import com.cheny.exception.DishOperationException;
import com.cheny.query.DishQuery;
import com.cheny.service.DishFlavorService;
import com.cheny.service.DishService;
import com.cheny.mapper.DishMapper;
import com.cheny.service.SetmealDishService;
import com.cheny.utils.AliOSSUtils;
import com.cheny.vo.DishVo;
import com.cheny.vo.PageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

/**
* @author Mlpnk
* @description 针对表【dish(明星人物表)】的数据库操作Service实现
* @createDate 2026-08-15 11:56:10
*/
@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
    implements DishService{

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private AliOSSUtils aliOSSUtils;

    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    public DishVo getDishById(Long id) {
        Dish dish = getById(id);
        DishVo dishVo = new DishVo();
        BeanUtils.copyProperties(dish,dishVo);
        return dishVo;
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        //根据id查询，再修改相应信息，最后update
        //如果要改成禁用，要检查套餐内是否有关联这个菜品
        if(status == StatusConstant.DISABLE){
            //检查套餐内是否有关联这个菜品
            Long count = setmealDishService.lambdaQuery()
                    .eq(SetmealDish::getDishId, id)
                    .isNotNull(SetmealDish::getSetmealId)
                    .count();
            if(count > 0) {
                throw new DishOperationException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }
        }
        Dish dish = getById(id);
        dish.setStatus(status);
        updateById(dish);
    }

    @Override
    public PageVo<DishVo> getDishPage(DishQuery dishQuery) {
        Integer pageNo = dishQuery.getPageNo();
        Integer pageSize = dishQuery.getPageSize();
        //初始化分页参数
        Page<DishVo> pageParam = new Page<>(pageNo,pageSize);
        //构造查询
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(dishQuery.getName()!=null,Dish::getName,dishQuery.getName())
                .eq(dishQuery.getStatus()!=null,Dish::getStatus,dishQuery.getStatus())
                .eq(dishQuery.getCategoryId()!=null,Dish::getCategoryId,dishQuery.getCategoryId());

        Page<DishVo> page = baseMapper.selectPageVo(pageParam,lambdaQueryWrapper);
        PageVo<DishVo> pageVo = new PageVo<>();
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        pageVo.setList(page.getRecords());
        return pageVo;
    }

    @Transactional
    @Override
    public void updateDish(DishDto dishDto, MultipartFile imageFile) {
        //检查一下id是否存在
        if(dishDto.getId() == null) throw new RuntimeException(MessageConstant.PARAM_ILLEGAL);
        //菜品的名字不能重复
        Long cnt = lambdaQuery().eq(Dish::getName,dishDto.getName())
                .ne(Dish::getId,dishDto.getId())
                .count();
        if(cnt>0){
            throw new DishOperationException(MessageConstant.PARAM_ILLEGAL);
        }
        // ============OSS图片处理核心逻辑============
        // 如果上传了新图片文件，调用oss上传，拿到新url，覆盖dishDto.image
        if(imageFile != null && !imageFile.isEmpty()){
            try {
                String newImageUrl = aliOSSUtils.upload(imageFile);
                dishDto.setImage(newImageUrl);
            } catch (Exception e) {
                //捕获oss上传IO异常，包装为业务异常抛出
                log.error(MessageConstant.UPLOAD_FAILED,e);
                throw new DishOperationException(MessageConstant.UPLOAD_FAILED);
            }
        }
        // 如果imageFile为null，代表用户没有上传新图片，dishDto.image里面保存的就是老的oss地址，直接往下复制属性即可
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);
        //先插主表
        updateById(dish);


        Long dishId = dishDto.getId();
        // 2. 再插子表:给每条口味表补上 dish_id,然后批量插入
        //先把旧的都删了  lambdaQuery().会默认当前这个类对应的实体，所以必须指定实体类，用公共工具类Wrappers
        dishFlavorService.remove(Wrappers.<DishFlavor>lambdaQuery().eq(DishFlavor::getDishId,dishDto.getId()));

        List<DishFlavor> flavors = dishDto.getFlavorsList();
        //口味选择如果为空就不添加
        if (CollUtil.isNotEmpty(flavors)) {
            for (DishFlavor e : flavors) {
                e.setDishId(dishId);// 关联主表主键
            }
            dishFlavorService.saveBatch(flavors);
        }

    }
    @Transactional
    @Override
    public void addDish(DishDto dishDto, MultipartFile imageFile) {
        //菜品的名字不能重复
        Long cnt = lambdaQuery().eq(Dish::getName,dishDto.getName()).count();
        if(cnt>0){
            throw new DishOperationException(MessageConstant.PARAM_ILLEGAL);
        }
        // ============OSS图片处理核心逻辑============
        // 如果上传了新图片文件，调用oss上传，拿到新url，覆盖dishDto.image
        if(imageFile != null && !imageFile.isEmpty()){
            try {
                String newImageUrl = aliOSSUtils.upload(imageFile);
                dishDto.setImage(newImageUrl);
            } catch (Exception e) {
                //捕获oss上传IO异常，包装为业务异常抛出
                log.error(MessageConstant.UPLOAD_FAILED,e);
                throw new DishOperationException(MessageConstant.UPLOAD_FAILED);
            }
        }
        // 如果imageFile为null，代表用户没有上传新图片，dishDto.image里面保存的就是老的oss地址，直接往下复制属性即可
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);
        //先插主表
        save(dish);//MyBatis-Plus 的 save() 会把自增主键回填到 dish 对象


        Long dishId = dish.getId();
        // 2. 再插子表:给每条口味表补上 dish_id,然后批量插入

        List<DishFlavor> flavors = dishDto.getFlavorsList();
        //口味选择如果为空就不添加
        if (CollUtil.isNotEmpty(flavors)) {
            for (DishFlavor e : flavors) {
                e.setDishId(dishId);// 关联主表主键
            }
            dishFlavorService.saveBatch(flavors);
        }
    }

    @Override
    public List<DishVo> getDishesById(Integer categoryId) {
       List<Dish> dishes =  lambdaQuery()
               .eq(Dish::getCategoryId,categoryId)
               .eq(Dish::getStatus, StatusConstant.ENABLE)
               .list();
       if(CollUtil.isEmpty(dishes)) {
           return null;//说明那个分类下没有菜品
       }
       //对于每一个dish都填充它的flavor
        //拿出所有的菜品id
        List<Long> dishesIdList =  dishes.stream()
                .map(Dish::getId)
                .collect(Collectors.toList());
       //一次性查询所有菜品对应的口味
        List<DishFlavor> allFlavors = dishFlavorService.lambdaQuery().in(DishFlavor::getDishId,dishesIdList).list();

        //根据dishId分组
        Map<Long,List<DishFlavor>> flavorMap = allFlavors.stream().collect(Collectors.groupingBy(DishFlavor::getDishId));
        //遍历菜品，从map里面取出对应口味，封装vo
        List<DishVo> list = BeanUtil.copyToList(dishes,DishVo.class);
        for(DishVo vo:list){
            vo.setFlavors(flavorMap.get(vo.getId()));
        }
        return list;
     }

    @Override
    @Transactional
    public void removeDishById(Long id) {
        //起售中的菜品不能删除
        Dish dish = getById(id);
        if(dish.getStatus() == StatusConstant.ENABLE){
            throw new DishOperationException(MessageConstant.DISH_ON_SALE);
        }
        //当前菜品关联了套餐，不能删除
        if(CollUtil.isNotEmpty(dishFlavorService.lambdaQuery().eq(DishFlavor::getDishId,id).list())){
            throw new DishOperationException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        removeById(id);
        //删除菜品对应口味
        dishFlavorService.remove(Wrappers.<DishFlavor>lambdaQuery().eq(DishFlavor::getDishId,id));
    }
}




