package com.cheny.vo;

import lombok.Data;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;




@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeVo  {

    /**
     * 主键
     */
    private Long id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 身份证号
     */
    private String idNumber;
    /**
     * 账号状态 1正常 0锁定
     */
    private Integer status;
    /**
     * 最后修改时间
     */
    private LocalDateTime updateTime;



}
