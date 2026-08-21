package com.cheny.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.cheny.entity.OrderDetail;
import com.cheny.service.OrderDetailService;
import com.cheny.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author Mlpnk
* @description 针对表【order_detail(约稿订单明细表)】的数据库操作Service实现
* @createDate 2026-08-15 11:56:10
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




