package com.cheny.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wx")
public class WxProperties {
    private String appId; // 微信小程序appid
    private String appSecret; // 微信小程序appsecret
    private String grantType = "authorization_code"; // 授权类型，固定值为 authorization_code
}
