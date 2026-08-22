package com.cheny.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.cheny.dto.AddressBookDto;
import com.cheny.entity.AddressBook;
import com.cheny.vo.AddressBookVo;
import jakarta.validation.Valid;

import java.util.List;

/**
* @author Mlpnk
* @description 针对表【address_book(用户交付地址表)】的数据库操作Service
* @createDate 2026-08-15 11:54:22
*/
public interface AddressBookService extends IService<AddressBook> {

    List<AddressBookVo> getList(Long userId);

    AddressBookVo getAddressById(Long addressId);

    void addAddress(@Valid AddressBookDto addressBookDto);

    void setDefault(Long addressId);

    AddressBookVo getDefault();

    void updateAddress(@Valid AddressBookDto addressBookDto);
}
