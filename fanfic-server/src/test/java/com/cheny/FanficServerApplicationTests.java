package com.cheny;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

@SpringBootTest
class FanficServerApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void  test() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //opsForValue返回ValueOperations对象，用于对redis进行字符串类型的数据操作
        HashOperations hashOperations = redisTemplate.opsForHash();
        //opsForHash返回HashOperations对象，用于对redis进行哈希类型的数据操作
        ListOperations listOperations = redisTemplate.opsForList();
        //opsForList返回ListOperations对象，用于对redis进行列表类型的数据操作
        SetOperations setOperations = redisTemplate.opsForSet();
        //opsForSet返回SetOperations对象，用于对redis进行集合类型的数据操作
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        //opsForZSet返回ZSetOperations对象，用于对redis进行有序集合类型的数据操作
    }
    @Test
    public void testRedis() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //修改键baike里的值为哈哈
        valueOperations.set("xixi", "哈哈");
        System.out.println(valueOperations.get("xixi"));

    }

}
