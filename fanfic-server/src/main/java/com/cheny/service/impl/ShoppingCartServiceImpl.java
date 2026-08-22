package com.cheny.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.cheny.dto.ShoppingCartDto;
import com.cheny.entity.Dish;
import com.cheny.entity.Setmeal;
import com.cheny.entity.ShoppingCart;
import com.cheny.service.DishService;
import com.cheny.service.SetmealService;
import com.cheny.service.ShoppingCartService;
import com.cheny.mapper.ShoppingCartMapper;
import com.cheny.utils.CurrentHolder;
import com.cheny.vo.ShoppingCartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Mlpnk
* @description 针对表【shopping_cart(约稿购物车表)】的数据库操作Service实现
* @createDate 2026-08-15 11:56:10
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService{

    final Long USER_ID = CurrentHolder.getCurrentId();

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public List<ShoppingCartVo> getShoppingCartList() {
        List<ShoppingCart> list = lambdaQuery().eq(ShoppingCart::getUserId, USER_ID).list();
        return list.stream().map(o -> {
            ShoppingCartVo vo = new ShoppingCartVo();
            BeanUtil.copyProperties(o, vo);
            return vo;
        }).toList();
    }

    @Override
    public void add(ShoppingCartDto shoppingCartDto) {
        Long userId = USER_ID;
        //1 查询购物车：用户id + 菜品id(可选)+套餐id(可选)+口味(可选)
        ShoppingCart shoppingCart = lambdaQuery()
                .eq(ShoppingCart::getUserId, userId)
                .eq(shoppingCartDto.getDishId() != null, ShoppingCart::getDishId, shoppingCartDto.getDishId())
                .eq(shoppingCartDto.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCartDto.getSetmealId())
                .eq(shoppingCartDto.getDishFlavor() != null && !shoppingCartDto.getDishFlavor().isBlank(),
                        ShoppingCart::getDishFlavor, shoppingCartDto.getDishFlavor())
                .one();

        // 购物车已有这条记录，数量+1
        if (shoppingCart != null) {
            shoppingCart.setNumber(shoppingCart.getNumber() + 1);
            // 直接更新查到的对象，自带id主键！！
            updateById(shoppingCart);
            return;
        }

        // ====== 购物车没有记录，新建对象！！原来漏 new ShoppingCart() 导致NPE ======
        ShoppingCart newCart = new ShoppingCart();
        newCart.setUserId(userId);
        newCart.setNumber(1);
        //dto的属性复制过来：dishId setmealId dishFlavor
        BeanUtil.copyProperties(shoppingCartDto, newCart);

        if(shoppingCartDto.getDishId() != null){
            //新增菜品购物车
            Dish dish = dishService.getById(shoppingCartDto.getDishId());
            if(dish == null){
                throw new RuntimeException("菜品不存在");
            }
            //只复制购物车需要的字段，不要全量copy Dish实体
            newCart.setName(dish.getName());
            newCart.setImage(dish.getImage());
            newCart.setAmount(dish.getPrice());
        }else if(shoppingCartDto.getSetmealId() != null){
            //新增套餐购物车
            Setmeal setmeal = setmealService.getById(shoppingCartDto.getSetmealId());
            if(setmeal == null){
                throw new RuntimeException("套餐不存在");
            }
            newCart.setName(setmeal.getName());
            newCart.setImage(setmeal.getImage());
            newCart.setAmount(setmeal.getPrice());
        }else {
            //dishId和setmealId都为空，非法参数
            throw new RuntimeException("商品id不能为空");
        }
        save(newCart);
    }

    @Override
    public void updateNum(Long id, Integer num) {
        ShoppingCart shoppingCart = getById(id);
        shoppingCart.setNumber(shoppingCart.getNumber() - num);
        updateById(shoppingCart);
    }


}




