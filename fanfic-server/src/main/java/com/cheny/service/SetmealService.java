package com.cheny.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.cheny.dto.SetmealDto;
import com.cheny.entity.Setmeal;
import com.cheny.query.SetmealQuery;
import com.cheny.vo.DishItemVo;
import com.cheny.vo.PageVo;
import com.cheny.vo.SetmealVo;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author Mlpnk
* @description 针对表【setmeal(明星组合/CP表)】的数据库操作Service
* @createDate 2026-08-15 11:56:10
*/
public interface SetmealService extends IService<Setmeal> {

    void updateStatus(Integer status, Long id);

    PageVo<SetmealVo> pageSetmeal(SetmealQuery setMealQuery);

    void addSetmeal(@Valid SetmealDto setmealDto, MultipartFile imageFile);

    void updateSetmeal(@Valid SetmealDto setmealDto, MultipartFile imageFile);

    List<DishItemVo> getDishItemVosById(Long id);

    void removeSetmealById(Long id);

    void removeSetmealByIds(List<Long> ids);

    //根据分类id查询套餐
    List<SetmealVo> getSetmealByCategoryId(Long categoryId);
}
