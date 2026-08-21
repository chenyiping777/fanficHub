package com.cheny.config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisConfig {

    @Bean
    // 配置redisTemplate:
    // 控制redisTemplate如何把Java对象转换成字节数组，
    // 再存进redis，
    // 以及从redis读出来时如何还原成Java对象（redis接收到字节数组会默认按照UTF-8编码还原成字符串，就转换成了我们可见的Java对象）
    // 所以我们就需要把传过去的Java对象按照UTF-8编码转换成JSON字符串再存进redis，
    // 编码译码所用的字符集是UTF-8，这样就保证了redis里存储的数据可读
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
//直接注入RedisConnectionFactory，自动配置已经解析完 yml，不需要自己写代码读取。
        log.info("redisTemplate init");

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 设置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 设置String序列化器
        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        // 设置key的序列化器
        redisTemplate.setKeySerializer(stringSerializer);


        // 设置String
        redisTemplate.setValueSerializer(stringSerializer);//String ： value字符串

        // 设置hash
        redisTemplate.setHashKeySerializer(stringSerializer);//hash ：field字符串
        redisTemplate.setHashValueSerializer(stringSerializer);//hash : value字符串

        //全部属性设置完成后执行的初始化方法
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}