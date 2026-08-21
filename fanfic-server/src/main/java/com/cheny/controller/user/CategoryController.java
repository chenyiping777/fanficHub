package com.cheny.controller.user;


import com.cheny.entity.Result;
import com.cheny.service.CategoryService;
import com.cheny.vo.CategoryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public Result allName(@RequestParam Integer type){
        log.info("分类类型：{}",type);
        List<CategoryVo> list = categoryService.selectByType(type);
        return Result.success(list);
    }
}
