package com.cheny.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.cheny.dto.DishDto;
import com.cheny.entity.Dish;
import com.cheny.query.DishQuery;
import com.cheny.vo.DishVo;
import com.cheny.vo.PageVo;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author Mlpnk
* @description 针对表【dish(明星人物表)】的数据库操作Service
* @createDate 2026-08-15 11:56:10
*/
public interface DishService extends IService<Dish> {

    //管理员点开编辑菜品时用的回显
    DishVo getDishById(Long id);

    //修改菜品上下架状态
    void updateStatus(Integer status, Long id);

    //分页查询菜品
    PageVo<DishVo> getDishPage(DishQuery dishQuery);

    //修改菜品信息
    void updateDish(@Valid DishDto dishDto, MultipartFile imageFile);

    //添加菜品
    void addDish(@Valid DishDto dishDto, MultipartFile imageFile);

    //根据分类id查询菜品  用户端点击菜品分类时用
    List<DishVo> getDishesById(Integer categoryId);

    void removeDishById(Long id);
}
