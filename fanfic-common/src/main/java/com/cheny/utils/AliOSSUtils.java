package com.cheny.utils;

import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.credentials.StaticCredentialsProvider;
import com.aliyun.sdk.service.oss2.models.PutObjectRequest;
import com.aliyun.sdk.service.oss2.transport.BinaryData;
import com.cheny.properties.AliOssProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Component
public class AliOSSUtils {

    @Autowired
    private AliOssProperties alioss;

    public String upload(MultipartFile multipartFile) throws IOException {

        String originalFilename = multipartFile.getOriginalFilename();
        //获取用户上传的原始文件名，用来构建上传到阿里云文件的唯一标识
        //文件必须依附于表单请求才能被浏览器发送出去，表单里面有multipart
        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM"))
                + "/" + UUID.randomUUID().toString().replace("-", "")//只包含字母和数字
                + "_" + originalFilename;//“2026/8/xxxxxxxxxx_1.jpg”

        InputStream inputStream = multipartFile.getInputStream();//转换成字节输入流

        StaticCredentialsProvider credentialsProvider = new StaticCredentialsProvider(
                alioss.getAccessKeyId(),
                alioss.getAccessKeySecret()
        );//credentialsProvider凭证提供者，证明有权限操作该用户的OSS bucket

        //  try-with-resources：不管 putObject 成功还是抛异常，client 都会自动 close
        //client客户端实例，负责发送所有的网络请求
        try (OSSClient client = OSSClient.newBuilder()
                .credentialsProvider(credentialsProvider)
                .region(alioss.getRegion())
                .endpoint(alioss.getEndpoint())
                .build()) //资源对象，只存账号的公共信息
        {

            PutObjectRequest request = PutObjectRequest.newBuilder()
                    .bucket(alioss.getBucketName())//一个客户端实例可以操作多个不同的bucket
                    .key(fileName)//文件在0SS里的完整路径
                    .body(BinaryData.fromStream(inputStream))//文件输入流
                    .build();

            client.putObject(request);//上传阿里云，request是单次动作的参数

        } catch (Exception e) {
            log.error("阿里云OSS文件上传失败，文件名：{}", originalFilename, e);
            throw new IOException("文件上传失败：" + e.getMessage(), e);
        }

        //拼接外网访问url
        return String.format("https://%s.%s/%s",
                alioss.getBucketName(),
                alioss.getEndpoint().replace("https://", ""),
                fileName);
    }
}