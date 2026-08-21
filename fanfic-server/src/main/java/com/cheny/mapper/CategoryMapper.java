package com.cheny.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cheny.entity.Category;
import com.cheny.vo.CategoryVo;

/**
* @author Mlpnk
* @description 针对表【category(人物/组合分类表)】的数据库操作Mapper
* @createDate 2026-08-15 11:56:10
* @Entity com.cheny.mapper.Category
*/
public interface CategoryMapper extends BaseMapper<Category> {

    Page<CategoryVo> selectPageVo(Page<CategoryVo> pageParam, LambdaQueryWrapper<Category> queryWrapper);
}




