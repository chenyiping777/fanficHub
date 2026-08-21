package com.cheny.controller.user;

import com.cheny.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userShopController")
@Slf4j
@RequestMapping("/user/shop")
public class ShopController {


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    //查询店铺状态
    @GetMapping
    public Result getShopStatus() {

        Integer status = (Integer) redisTemplate.opsForValue().get("shopStatus");
        //因为存的时候不是1就是0，所以不会出现null
        log.info("查询店铺状态{}", status.equals(1) ? "营业中" : "打烊中");
        return Result.success(status);
    }


}
