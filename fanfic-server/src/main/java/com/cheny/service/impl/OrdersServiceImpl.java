package com.cheny.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.cheny.entity.Orders;
import com.cheny.service.OrdersService;
import com.cheny.mapper.OrdersMapper;
import org.springframework.stereotype.Service;

/**
* @author Mlpnk
* @description 针对表【orders(约稿订单主表)】的数据库操作Service实现
* @createDate 2026-08-15 11:56:10
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

}




