package com.cheny.controller.user;


import com.cheny.entity.Result;
import com.cheny.service.DishService;
import com.cheny.vo.DishVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


/**
 * 用户端 — 菜品（人物）查询接口
 *
 * <p>缓存策略：
 * <ul>
 *   <li>数据结构：Hash，key = dish:{categoryId}，field = 菜品id，value = DishVo 对象</li>
 *   <li>过期时间：1 小时 + 随机 0~10 分钟（防止大量缓存同时失效 → 缓存雪崩）</li>
 *   <li>空值缓存：分类下无菜品时缓存空列表 5 分钟（防止缓存穿透）</li>
 * </ul>
 */
@RestController
@Slf4j
@RequestMapping("/user/dish")
public class DishController {

    /** 空值缓存前缀，与正常缓存 key 区分 */
    private static final String EMPTY_PREFIX = "dish:empty:";

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据分类 id 查询菜品列表（带 Redis 缓存）
     * 先查 Redis：命中dish:empty:{cid} → 直接返回空列表，不进 DB；
     * Redis 没有 empty 标记，读取 hash dish:{cid}缓存，如果缓存命中，直接返回 VO；
     * Redis 全部没命中，进入这段 DB 查询逻辑；
     * 如果方法返回null：代表 DB 无菜品，写入dish:empty:{cid}空标记；
     * 如果返回List<DishVo>：把这批 VO 写入 Hash 缓存 dish:{categoryId}
     */
    @GetMapping("/list")
    public Result list(Integer categoryId) {
        String key = "dish:" + categoryId;
        String emptyKey = EMPTY_PREFIX + categoryId;

        // ========== 1. 检查空值缓存（防穿透） ==========
        Boolean hasEmptyKey = redisTemplate.hasKey(emptyKey);
        if (hasEmptyKey == true) {
            log.debug("空值缓存命中: {}", emptyKey);
            return Result.success(Collections.emptyList());
        }

        // ========== 2. 查 Hash 缓存 ==========
        List<Object> cached = redisTemplate.opsForHash().values(key);
        //查的是这个分类下所有的菜品，跳过了所有的field，直接返回所有的value
        if (cached != null && !cached.isEmpty()) {
            log.debug("菜品缓存命中: {}", key);
            // opsForHash().values() 返回 List<Object>，需要强转为 List<DishVo>
            List<DishVo> dishVos = cached.stream()
                    .map(o -> (DishVo) o)
                    .toList();
            return Result.success(dishVos);
        }

        // ========== 3. 缓存未命中，查数据库 ==========
        List<DishVo> dishVos = dishService.getDishesById(categoryId);

        if (dishVos == null || dishVos.isEmpty()) {
            // ---- 数据库也没数据 → 缓存空值 5 分钟（防穿透） ----
            // 套餐不存在或没有菜品 → 缓存空值 5 分钟
            redisTemplate.opsForValue().set(emptyKey, "", Duration.ofMinutes(5));
            log.debug("缓存空值: {}", emptyKey);
            return Result.success(Collections.emptyList());
        }

        // ---- 有数据 → 写入 Hash 缓存 ----
        Map<String, Object> dishMap = new HashMap<>();
        for (DishVo vo : dishVos) {
            String key2 = vo.getId().toString();
            dishMap.putIfAbsent(key2, vo);
        }

        // 随机过期时间：1小时 + 随机0~10分钟，防止大量key同时过期（缓存雪崩）
        long expireSeconds = 3600 + ThreadLocalRandom.current().nextLong(600);
        redisTemplate.expire(key, Duration.ofSeconds(expireSeconds));
        log.debug("写入菜品缓存: {}，过期{}s", key, expireSeconds);

        return Result.success(dishVos);
    }

}
