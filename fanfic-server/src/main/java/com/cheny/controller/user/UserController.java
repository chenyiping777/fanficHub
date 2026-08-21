package com.cheny.controller.user;


import com.cheny.entity.Result;
import com.cheny.entity.User;
import com.cheny.service.UserService;
import com.cheny.utils.JwtUtil;
import com.cheny.vo.UserLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("user/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("login")//提交凭证，做数据校验
    public Result login(@RequestBody String code){//code是微信服务器（调用wx.login）返回的临时授权凭证，一次性，短期有效
        log.info("code: {}", code);
        //我们需要拿着这个凭证，加上小程序的appId，appSecret，去微信服务器（调用wx.login）换session_key和openid
        //微信服务器返回的openid（去数据库查是否存在该用户），我们需要保存起来，用于后续的用户校验和用户信息获取
        //如果数据库中不存在该用户，我们需要创建一个新用户并保存到数据库中
        //最后返回给小程序一个token，用于后续的用户校验。管理员也有个登录接口，
        // 登录成功后也可以获取token，但生成的token和用户不同，
        // 一个是根据用户openid生成的，一个是根据管理员id生成的
        User user = userService.login(code);
        //返回给小程序一个token

        String token = jwtUtil.createToken(user.getId(),"user");

        UserLoginVo userLoginVo = new UserLoginVo();
        userLoginVo.setId(user.getId());
        userLoginVo.setOpenId(user.getOpenid());
        userLoginVo.setToken(token);

        return Result.success(userLoginVo);
    }
}
