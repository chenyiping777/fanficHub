package com.cheny.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.cheny.entity.AddressBook;
import com.cheny.service.AddressBookService;
import com.cheny.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

/**
* @author Mlpnk
* @description 针对表【address_book(用户交付地址表)】的数据库操作Service实现
* @createDate 2026-08-15 11:54:22
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

}




