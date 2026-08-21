package com.cheny.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.cheny.constant.MessageConstant;
import com.cheny.entity.User;
import com.cheny.exception.BaseException;
import com.cheny.exception.LoginFailedException;
import com.cheny.properties.WxProperties;
import com.cheny.service.UserService;
import com.cheny.mapper.UserMapper;
import com.cheny.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
* @author Mlpnk
* @description 针对表【user(C端约稿用户表)】的数据库操作Service实现
* @createDate 2026-08-15 11:56:10
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private HttpClientUtil httpClientUtil;

    @Autowired
    private WxProperties wxProperties;
    // 微信登录接口地址
    public  static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";


    @Override
    public User login(String code) {
        String openId = getOpenId(code);
        //判断openId是否为空，为空表示登录失败，抛出业务异常
        if(openId == null)
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);

        //去数据库查openid是否存在
        long count = lambdaQuery().eq(User::getOpenid, openId).count();
        if (count == 0) {
            // 不存在，保存用户
            User user = new User();
            user.setOpenid(openId);

            save(user);//自动回填
            return user;
        }
            // 存在，返回用户
        return getOne(lambdaQuery().eq(User::getOpenid, openId));

    }

    public String getOpenId(String code) {
        //连接微信服务器，获取session_key和openid
        Map<String,String> params = new HashMap<>();
        params.put("code", code);
        params.put("appid", wxProperties.getAppId());
        params.put("secret", wxProperties.getAppSecret());
        params.put("grant_type", wxProperties.getGrantType());
        String res = httpClientUtil.doGet(WX_LOGIN_URL, params);
        //方法第二个参数要求是 Map<String,String>。
        //paramMap 作用：存放URL 查询参数，就是 url 后面 ?key=value&key2=value2 这一部分。
        //Map 的 key = 查询参数名
        //Map 的 value = 查询参数值

        //返回的是json字符串，需要解析
        JSONObject jsonObject = new JSONObject(res);
        return jsonObject.getStr("openid");
    }

}




