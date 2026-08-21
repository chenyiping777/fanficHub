package com.cheny;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement // 开启注解方式事务管理
@Slf4j
//@EnableCaching // 开发缓存注解功能
//@EnableScheduling // 开启任务调度
@MapperScan("com.cheny.mapper")
public class FanficApplication {
    public static void main(String[] args) {
        SpringApplication.run(FanficApplication.class, args);
        log.info("server started");
    }
}