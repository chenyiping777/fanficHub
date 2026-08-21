package com.cheny.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.cheny.dto.CategoryDto;
import com.cheny.entity.Category;
import com.cheny.query.CategoryQuery;
import com.cheny.vo.CategoryVo;
import com.cheny.vo.PageVo;

import java.util.List;

/**
* @author Mlpnk
* @description 针对表【category(人物/组合分类表)】的数据库操作Service
* @createDate 2026-08-15 11:56:10
*/
public interface CategoryService extends IService<Category> {

    PageVo<CategoryVo> getCategoryPage(CategoryQuery categoryQuery);

    void updateStatus(Integer status, Long id);

    void updateCategory(CategoryDto categoryDto);

    void addCategory(CategoryDto categoryDto);

    List<CategoryVo> selectByType(Integer type);

    void removeCategoryById(Long id);
}
