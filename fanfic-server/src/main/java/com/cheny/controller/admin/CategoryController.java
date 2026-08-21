package com.cheny.controller.admin;


import com.cheny.dto.CategoryDto;
import com.cheny.entity.Result;
import com.cheny.query.CategoryQuery;
import com.cheny.service.CategoryService;
import com.cheny.vo.CategoryVo;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //分页展示菜品，支持输入分类名称，输入分类类型查找
    @GetMapping("/page")
    public Result page(@RequestBody CategoryQuery categoryQuery){
        log.info("分页信息：{}",categoryQuery);
        return Result.success(categoryService.getCategoryPage(categoryQuery));
    }

    //输入分类类型查找,下拉框渲染所有分类名称
    @GetMapping("/{type}")
    public Result allName(@PathVariable Integer type){
        log.info("分类类型：{}",type);
        List<CategoryVo> list = categoryService.selectByType(type);
        return Result.success(list);
    }

    //删除分类
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        categoryService.removeCategoryById(id);
        return Result.success();
    }


    //禁用启用分类
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status,@RequestParam Long id){
        log.info("要禁用/启用的分类id为: {}",id );
        categoryService.updateStatus(status,id);
        return Result.success();
    }

    //修改分类
    @PutMapping
    public Result update(@RequestBody @Valid CategoryDto categoryDto){
        log.info("修改后的分类为为: {}",categoryDto);
        categoryService.updateCategory(categoryDto);
        return Result.success();
    }

    //对分类类型下的进行新增
    @PostMapping("/{type}")
    public Result addType(@RequestBody @Valid CategoryDto categoryDto,@PathVariable Integer type){
        categoryDto.setType(type);
        categoryService.addCategory(categoryDto);
        return Result.success();
    }

//分类管理（菜品分类，套餐分类）
//菜品管理
//套餐管理（套餐里包含多个菜品，套餐表和菜品表式多对多关系，通常选择设计一个子表setmeal_dish）
}
