package com.cheny.config;

import com.cheny.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    // 注册拦截器：除登录/注册外，所有接口都需要 token 校验,前端携带 token
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**") // 拦截所有接口
                .excludePathPatterns(
                        "/admin/employee/login",       // 登录接口放行
                        "/admin/register",    // 注册放行
                        // ---- Swagger / OpenAPI ----
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/v3/api-docs.yaml",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/**"
                );
    }

    @Bean
//    把这个方法返回的对象，注册为 Spring 容器中的 Bean。
//    @Bean必须写在@Configuration类（或者@Component类）内部才会生效。
//@Bean一般也都是跟着@Configuration一起用的，语义上也更清晰
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*")); // 允许任意来源（file:// 的 Origin 为 null 也放行）
        config.setAllowedMethods(List.of("*"));        // 允许任意请求方式 GET/POST/PUT/DELETE...
        config.setAllowedHeaders(List.of("*"));        // 允许携带任意请求头（包括 token）
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
