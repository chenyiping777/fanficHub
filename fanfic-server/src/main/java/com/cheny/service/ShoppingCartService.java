package com.cheny.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.cheny.dto.ShoppingCartDto;
import com.cheny.entity.ShoppingCart;
import com.cheny.vo.ShoppingCartVo;

import java.util.List;

/**
* @author Mlpnk
* @description 针对表【shopping_cart(约稿购物车表)】的数据库操作Service
* @createDate 2026-08-15 11:56:10
*/
public interface ShoppingCartService extends IService<ShoppingCart> {

    List<ShoppingCartVo> getShoppingCartList();

    void add(ShoppingCartDto shoppingCartDto);

    void updateNum(Long id, Integer num);
}
