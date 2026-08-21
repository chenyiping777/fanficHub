package com.cheny.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * springdoc-openapi-starter-webmvc-ui 3.x 已自动注册 Swagger UI 资源映射，
 * 无需手动 addResourceHandler，否则路径不匹配反而会冲突。
 */
@Configuration
public class SpringDocWebConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("fanficHub API")
                        .version("1.0")
                        .description("同人文约稿平台接口文档"));
    }
}