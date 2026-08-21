package com.cheny.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.cheny.entity.ShoppingCart;
import com.cheny.service.ShoppingCartService;
import com.cheny.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;

/**
* @author Mlpnk
* @description 针对表【shopping_cart(约稿购物车表)】的数据库操作Service实现
* @createDate 2026-08-15 11:56:10
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService{

}




