package com.cheny.controller.user;


import com.cheny.dto.ShoppingCartDto;
import com.cheny.entity.Result;
import com.cheny.service.ShoppingCartService;
import com.cheny.vo.ShoppingCartVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    //购物车管理


    //查看购物车
    @GetMapping("/list")
    public Result getShoppingCartList() {
        List<ShoppingCartVo> shoppingCartList = shoppingCartService.getShoppingCartList();
        return Result.success(shoppingCartList);
    }

    //删除购物车，清空购物车
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        shoppingCartService.removeById(id);
        return Result.success();
    }

    //删除购物车，清空购物车
    @DeleteMapping
    public Result deleteAll(@RequestBody List<Long> ids) {
        shoppingCartService.removeByIds(ids);
        return Result.success();
    }

    //添加购物车
    @PostMapping
    public Result add(@RequestBody ShoppingCartDto shoppingCartDto) {
        log.info("添加购物车，商品信息为：{}", shoppingCartDto);
        shoppingCartService.add(shoppingCartDto);
        return Result.success();
    }

    //修改购物车某一条数量
    @PutMapping
    public Result updateNum(@RequestParam Long id, @RequestParam Integer num){
        log.info("修改购物车某一条数量，id为：{},数量为：{}", id, num);
        shoppingCartService.updateNum(id,num);
        return Result.success();
    }



}
