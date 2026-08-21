package com.cheny.utils;


import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.cheny.properties.JwtProperties;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Autowired
    private JwtProperties jwtProperties;

//    //获取加密秘钥   SecretKey是一个接口
//    //内部会做密钥长度合法性校验，不符合对应 HS256/HS384/HS512
//    //算法最低长度直接抛出IllegalArgumentException，在密钥构造阶段就报错
//    public static SecretKey getSecretKey(){
//        return Keys.hmacShaKeyFor(secretStr.getBytes());
//        //返回对象是一个实现了这个接口的类的对象
//    }

    public  String createToken(Long userId,String role){
        Map<String,Object> payload = new HashMap<>();//只有一组键值对
        payload.put("userId",userId);//是自定义的私有字段
        payload.put("role", role);
        payload.put("exp", jwtProperties.getExpire() + System.currentTimeMillis()/1000);
        return JWTUtil.createToken(payload, jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    //校验令牌是否合法
    public  boolean verifyToken(String token){
        return JWTUtil.verify(token, jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
    //从令牌中取出载荷部分
    public JWTPayload getPayload(String token){
        return JWTUtil.parseToken(token).getPayload();
    }


//
//    public String createToken(Integer userId){
//        //生成令牌，把关键信息存入头部和载荷
//        //1.生成过期时间
////        DateUtil.current()//获取当前时间戳，返回基准时间到现在的毫秒值
////        DateUtil.date()//获取当前时间，返回Date类型
//       long expire =  DateUtil.current()+expireTime;
//       Date expireDate = new Date(expire);
//
//        Map<String, Object> claims = new HashMap<>();
//        //是一行多列吗，claims到底是用来干什么的，存了什么东西
//
    ////        载荷的数据部分
    ////        claims.put("userId", userId);//属于自定义声明
//
//        return Jwts.builder()
//                .claims(claims)              // 设置自定义载荷
//                .subject(userId.toString())  // 标准主题字段，等价于往map里放sub
//                .expiration(expireDate)      // 设置过期时间
//                .signWith(getSecretKey())    //获取密钥
//                .compact();                  // 压缩生成最终token字符串
//
//
//    }

//    //校验令牌，返回userId
//    public Integer getUserId(String token){
//        // 1.前置判断token是否为空
//        if (token == null || token.isBlank()) {
//            throw new RuntimeException("令牌不能为空，请重新登录");
//        }
//
//        try {
//            String sub = Jwts.parser()//创建JWT 解析构建器，用来后续配置校验规则、解析 Token 字符串
//                    .verifyWith((javax.crypto.SecretKey) getSecretKey())//声明：要用这个密钥验证 JWT 的签名合法性。校验错误就不会往下执行，抛出异常？
//                    .build()//获取一个构造器
//                    .parseSignedClaims(token)//验签校验（令牌为空，格式错误，被篡改，过期），解析 Token 结构
//                    .getPayload()//取出载荷
//                    .getSubject();//在载荷里取出用户唯一标识
//
//
//            // 2.sub非空校验，防止空字符串转换报错
//            if (sub == null || sub.isBlank()) {
//                throw new RuntimeException("令牌内用户标识为空，认证失败");
//            }
//            return Integer.valueOf(sub);
//        } catch (ExpiredJwtException e) {
//            throw new RuntimeException("令牌已过期，请重新登录");
//        } catch (SignatureException e) {
//            throw new RuntimeException("令牌签名非法，数据被篡改");
//        } catch (MalformedJwtException e) {
//            throw new RuntimeException("令牌格式错误");
//        } catch (NumberFormatException e) {
//            throw new RuntimeException("令牌中用户ID格式非法");
//        } catch (Exception e) {
//            throw new RuntimeException("令牌校验失败，请重新登录");
//        }
//    }





}
