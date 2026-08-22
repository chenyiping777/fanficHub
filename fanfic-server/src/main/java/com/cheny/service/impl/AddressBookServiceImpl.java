package com.cheny.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.cheny.constant.StatusConstant;
import com.cheny.dto.AddressBookDto;
import com.cheny.entity.AddressBook;
import com.cheny.service.AddressBookService;
import com.cheny.mapper.AddressBookMapper;
import com.cheny.utils.CurrentHolder;
import com.cheny.vo.AddressBookVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author Mlpnk
* @description 针对表【address_book(用户交付地址表)】的数据库操作Service实现
* @createDate 2026-08-15 11:54:22
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{


    final Long userId = CurrentHolder.getCurrentId();
    @Override
    public List<AddressBookVo> getList(Long userId) {
        List<AddressBook> list = lambdaQuery().eq(AddressBook::getUserId,userId)
                .orderByDesc(AddressBook::getIsDefault)//默认地址的这个字段为1，所以按照这个降序排列，就能实现，默认地址排在最上面
                .list();
        //结果封装
        return list.stream().map(o->{
            AddressBookVo vo = new AddressBookVo();
            BeanUtil.copyProperties(o,vo);
            return vo;
        }).toList();
    }

    @Override
    public AddressBookVo getAddressById(Long addressId) {
        AddressBook o = getById(addressId);
        AddressBookVo vo = new AddressBookVo();
        BeanUtil.copyProperties(o,vo);
        return vo;
    }

    @Override
    public void addAddress(AddressBookDto addressBookDto) {

        AddressBook addressBook = new AddressBook();
        BeanUtil.copyProperties(addressBookDto,addressBook);
        addressBook.setUserId(userId);
        save(addressBook);
    }

    @Override
    public void setDefault(Long addressId) {
        //先把这个用户的所有地址取消默认
        lambdaUpdate().eq(AddressBook::getUserId,userId)
                .set(AddressBook::getIsDefault,StatusConstant.NOT_DEFAULT)
                .update();
        //再把选中的这条地址设置为默认
        lambdaUpdate().eq(AddressBook::getId,addressId)
                .set(AddressBook::getIsDefault,StatusConstant.IS_DEFAULT)
                .update();

    }

    @Override
    public AddressBookVo getDefault() {

        AddressBook addressBook = lambdaQuery().eq(AddressBook::getIsDefault,StatusConstant.IS_DEFAULT)
                .eq(AddressBook::getUserId,userId)
                .one();
        AddressBookVo addressBookVo = new AddressBookVo();
        BeanUtil.copyProperties(addressBook,addressBookVo);
        return addressBookVo;

    }

    @Override
    public void updateAddress(AddressBookDto addressBookDto) {

        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(userId);
        BeanUtil.copyProperties(addressBookDto,addressBook);
        updateById(addressBook);
    }
}




