package com.cheny.controller.user;

import com.cheny.entity.Result;
import com.cheny.service.SetmealService;
import com.cheny.vo.DishItemVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 用户端 — 套餐（组合/CP）查询接口
 *
 * <p>缓存策略：
 * <ul>
 *   <li>数据结构：String，key = setmeal:{id}，value = List&lt;DishItemVo&gt; JSON</li>
 *   <li>过期时间：1 小时 + 随机 0~10 分钟</li>
 *   <li>空值缓存：套餐不存在时缓存空字符串 5 分钟（防穿透）</li>
 * </ul>
 */
@RestController
@Slf4j
@RequestMapping("/user/setmeal")
public class SetMealController {

    private static final String EMPTY_PREFIX = "setmeal:empty:";

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据套餐 id 查询套餐中的菜品（带 Redis 缓存）
     *
     * <p>查询流程：
     * 1. 先查空值缓存 → 命中说明套餐不存在
     * 2. 再查 String 缓存 → 命中直接返回
     * 3. 都未命中 → 查数据库 → 写缓存
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        String key = "setmeal:" + id;
        String emptyKey = EMPTY_PREFIX + id;


        // ========== 1. 查 String 缓存 ==========
        // RedisTemplate 的 value 序列化器会自动反序列化回 List<DishItemVo>
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            log.debug("套餐缓存命中: {}", key);
            // 泛型擦除，强转为 List
            @SuppressWarnings("unchecked")
            List<DishItemVo> dishItemVos = (List<DishItemVo>) cached;
            return Result.success(dishItemVos);
        }

        // ========== 2. 缓存未命中，查数据库 ==========
        List<DishItemVo> dishItemVos = setmealService.getDishItemVosById(id);

        if (dishItemVos == null || dishItemVos.isEmpty()) {
            // 套餐不存在或没有菜品 → 缓存空值 5 分钟
            redisTemplate.opsForValue().set(emptyKey, "", Duration.ofMinutes(5));
            log.debug("缓存空值: {}", emptyKey);
            return Result.success(Collections.emptyList());
        }

        // ---- 有数据 → 写入 String 缓存 ----
        redisTemplate.opsForValue().set(key, dishItemVos);

        // 随机过期：1小时 + 随机0~10分钟
        long expireSeconds = 3600 + ThreadLocalRandom.current().nextLong(600);
        redisTemplate.expire(key, Duration.ofSeconds(expireSeconds));
        log.debug("写入套餐缓存: {}，过期{}s", key, expireSeconds);

        return Result.success(dishItemVos);
    }
}
