package com.cheny.controller.user;


import com.cheny.dto.AddressBookDto;
import com.cheny.entity.Result;
import com.cheny.service.AddressBookService;
import com.cheny.utils.CurrentHolder;
import com.cheny.vo.AddressBookVo;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    //查询地址列表
    @GetMapping("/list")
    public Result getAddressBook() {
        //先取出当前正在操作的用户id
        Long userId = CurrentHolder.getCurrentId();
        List<AddressBookVo> list = addressBookService.getList(userId);
        return Result.success(list);
    }

    //根据id查询地址回显
    @GetMapping("/{addressId}")
    public Result getAddressBookById(@PathVariable Long addressId) {
        AddressBookVo vo = addressBookService.getAddressById(addressId);
        return Result.success(vo);
    }

    //新增地址
    @PostMapping
    public Result add(@RequestBody @Valid AddressBookDto addressBookDto) {
        log.info("要新增的地址信息：{}", addressBookDto);
        addressBookService.addAddress(addressBookDto);
        return Result.success();
    }


    //删除地址
    @DeleteMapping("/{addressId}")
    public Result delete(@PathVariable Long addressId) {
        addressBookService.removeById(addressId);
        return Result.success();
    }

    //设置默认地址
    @PostMapping("/{addressId}")
    public Result setDefault(@PathVariable Long addressId) {
        addressBookService.setDefault(addressId);
        return Result.success();

    }
    //查询默认地址
    @GetMapping
    public Result getDefault(){
        AddressBookVo addressBookVo = addressBookService.getDefault();
        return Result.success(addressBookVo);

    }
    //修改地址
    @PutMapping
    public Result update(@RequestBody @Valid AddressBookDto addressBookDto){
        addressBookService.updateAddress(addressBookDto);
        return Result.success();
    }
}
