package com.cheny.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cheny.entity.Setmeal;
import com.cheny.vo.DishItemVo;
import com.cheny.vo.SetmealVo;

import java.util.List;

/**
* @author Mlpnk
* @description 针对表【setmeal(明星组合/CP表)】的数据库操作Mapper
* @createDate 2026-08-15 11:56:10
* @Entity com.cheny.mapper.Setmeal
*/
public interface SetmealMapper extends BaseMapper<Setmeal> {

    Page<SetmealVo> selectPageSetmeal(Page<SetmealVo> pageParam, LambdaQueryWrapper<Setmeal> lambdaQueryWrapper);

    List<DishItemVo> getDishItemBySetmealId(Long id);
}




