package com.cheny.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.cheny.entity.User;

/**
* @author Mlpnk
* @description 针对表【user(C端约稿用户表)】的数据库操作Service
* @createDate 2026-08-15 11:56:10
*/
public interface UserService extends IService<User> {

    User login(String code);
}
