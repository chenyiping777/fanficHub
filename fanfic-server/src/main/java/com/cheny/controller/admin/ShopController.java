package com.cheny.controller.admin;


import com.cheny.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@Slf4j
@RequestMapping("/admin/shop")
public class ShopController {


    public static final String SHOP_STATUS_KEY = "shopStatus";
    @Autowired
    private RedisTemplate redisTemplate;

    //修改店铺状态
    @PutMapping("/{status}")
    public Result updateShopStatus(@PathVariable Integer status) {
        log.info("更新店铺状态为: {}", status == 1 ? "营业中" : "打烊中");
        redisTemplate.opsForValue().set(SHOP_STATUS_KEY, status );
        return Result.success();
    }

    //查询店铺状态
    @GetMapping
    public Result getShopStatus() {

        Integer status = (Integer) redisTemplate.opsForValue().get(SHOP_STATUS_KEY);
        //因为存的时候不是1就是0，所以不会出现null
        log.info("查询店铺状态{}", status == 1 ? "营业中" : "打烊中");
        return Result.success(status);
    }


}
