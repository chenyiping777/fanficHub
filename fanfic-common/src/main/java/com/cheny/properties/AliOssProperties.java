package com.cheny.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "alioss")
public class AliOssProperties {

    private String endpoint;//OSS存储空间访问域名
    private String accessKeyId;//阿里云身份凭证ID
    private String accessKeySecret;//拿着ID和密码才有权限操作上传，删除文件
    private String bucketName;//存储空间名称，一个账号可以有多个bucket
    private String region;//这个bucket所属区域

}
