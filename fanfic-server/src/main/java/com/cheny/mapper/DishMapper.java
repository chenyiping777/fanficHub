package com.cheny.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cheny.entity.Dish;
import com.cheny.vo.DishVo;

/**
* @author Mlpnk
* @description 针对表【dish(明星人物表)】的数据库操作Mapper
* @createDate 2026-08-15 11:56:10
* @Entity com.cheny.mapper.Dish
*/
public interface DishMapper extends BaseMapper<Dish> {

    Page<DishVo> selectPageVo(Page<DishVo> pageParam, LambdaQueryWrapper<Dish> lambdaQueryWrapper);
}




