package com.cheny.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.cheny.constant.MessageConstant;
import com.cheny.constant.StatusConstant;
import com.cheny.dto.CategoryDto;
import com.cheny.entity.Category;
import com.cheny.entity.Dish;
import com.cheny.entity.Setmeal;
import com.cheny.exception.BaseException;
import com.cheny.exception.CategoryOperationException;
import com.cheny.query.CategoryQuery;
import com.cheny.service.CategoryService;
import com.cheny.mapper.CategoryMapper;
import com.cheny.service.DishService;
import com.cheny.service.SetmealService;
import com.cheny.vo.CategoryVo;
import com.cheny.vo.PageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Mlpnk
* @description 针对表【category(人物/组合分类表)】的数据库操作Service实现
* @createDate 2026-08-15 11:56:10
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{


    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;


    @Override
    public PageVo<CategoryVo> getCategoryPage(CategoryQuery categoryQuery) {
        Integer pageNo = categoryQuery.getPageNo();
        Integer pageSize = categoryQuery.getPageSize();
        Page<CategoryVo> pageParam = new Page<>(pageNo,pageSize);

        //查询
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryQuery.getName()!=null,Category::getName,categoryQuery.getName())
                .eq(categoryQuery.getType()!=null,Category::getType,categoryQuery.getType())
                .orderByAsc(Category::getSort);

        //分页
        Page<CategoryVo> page = baseMapper.selectPageVo(pageParam,queryWrapper);

        //封装分页
        PageVo<CategoryVo> pageVo = new PageVo<>();
        pageVo.setPages(page.getPages());
        pageVo.setTotal(page.getTotal());
        pageVo.setList(page.getRecords());
        return pageVo;

    }


    @Override
    public void updateStatus(Integer status, Long id) {
        //首先根据id查到相应的分类信息
        Category category = getById(id);
        //- type=1：检查dish表，若存在该分类下状态为启用的菜品，禁止禁用该分类
        //- type=2：检查setmeal表，若存在该分类下状态为启用的套餐，禁止禁用该分类
        //启用正常改，禁用需要检查关联的菜品或套餐
        if (status == StatusConstant.DISABLE) {
            if (category.getType() == 1) {
                // 检查dish表，若存在该分类下状态为启用的菜品，禁止禁用该分类
                long dishCount = dishService.lambdaQuery().eq(Dish::getCategoryId, id).count();
                if (dishCount > 0) {
                    throw new CategoryOperationException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
                }
            } else {
                // 检查setmeal表，若存在该分类下状态为启用的套餐，禁止禁用该分类
                long setmealCount = setmealService.lambdaQuery().eq(Setmeal::getCategoryId, id).count();
                if (setmealCount > 0) {
                    throw new CategoryOperationException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
                }
            }
        }
       //启用正常改
        category.setStatus(status);
        updateById(category);
    }

    @Override
    public void updateCategory(CategoryDto categoryDto) {
        //检查一下id是否存在
        if(categoryDto.getId() == null) throw new RuntimeException(MessageConstant.PARAM_ILLEGAL);
        //分类名称必须是唯一的
        String name = categoryDto.getName();
        Long cnt = lambdaQuery().eq(Category::getName,name)
                .ne(Category::getId,categoryDto.getId())
                .count();
        if(cnt>0) throw new BaseException(MessageConstant.PARAM_ILLEGAL);
        Category category = new Category();
        BeanUtils.copyProperties(categoryDto,category);
        updateById(category);
    }

    @Override
    public void addCategory(CategoryDto categoryDto) {
        //检查分类名称是否唯一
        String name = categoryDto.getName();
        Long cnt = lambdaQuery().eq(Category::getName,name)
                .ne(Category::getId,categoryDto.getId())
                .count();
        if(cnt>0) throw new BaseException(MessageConstant.PARAM_ILLEGAL);
        Category category = new Category();
        BeanUtils.copyProperties(categoryDto,category);
        baseMapper.insert(category);
    }

    @Override
    public List<CategoryVo> selectByType(Integer type) {
        List<Category> categoryList = lambdaQuery().eq(Category::getType, type)
                .eq(Category::getStatus, StatusConstant.ENABLE)
                .list();
        //集合拷贝  stream+单个对象拷贝
        return categoryList.stream().map(
                category -> {
                    CategoryVo vo = new CategoryVo();
                    BeanUtils.copyProperties(category, vo);
                    return vo;
                }
        ).toList();
    }

    @Override
    public void removeCategoryById(Long id) {

        //级联删除，要先判断type，然后再操作关联表记录的删除
        Category category = getById(id);
        if (category.getType() == 1) {
            // 删除菜品分类
            long dishCount =   dishService.lambdaQuery().eq(Dish::getCategoryId, id).count();
            if (dishCount > 0) {
                // 如果有菜品则抛出异常
                throw new CategoryOperationException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
            }
        } else {
            // 删除套餐分类
            long setmealCount = setmealService.lambdaQuery().eq(Setmeal::getCategoryId, id).count();
            if (setmealCount > 0) {
                // 如果有套餐则抛出异常
                throw new CategoryOperationException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
            }
        }
        removeById(id);
    }


}




