package com.cheny.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 平台后台员工表
* @TableName employee
*/
@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(name = "Employee", description = "平台后台员工表")

public class Employee implements Serializable {

    /**
    * 主键
    */
    @NotNull(message="[主键]不能为空")
    @Schema(description = "主键")
    private Long id;
    /**
    * 姓名
    */
    @Size(max= 32,message="编码长度不能超过32")
    @Schema(description = "姓名")
    private String name;
    /**
    * 用户名
    */
    @NotBlank(message="[用户名]不能为空")
    @Size(max= 32,message="编码长度不能超过32")
    @Schema(description = "用户名(要求唯一)")
    private String username;
    /**
    * 密码
    */
    @Size(max= 64,message="编码长度不能超过64")
    @Schema(description = "密码")
    private String password;
    /**
    * 手机号
    */
    @Size(max= 11,message="编码长度不能超过11")
    @Schema(description = "手机号")
    private String phone;
    /**
    * 性别
    */
    @NotNull(message="[性别]不能为空")
    @Schema(description = "性别")
    private Integer sex;
    /**
    * 身份证号
    */
    @Size(max= 18,message="编码长度不能超过18")
    @Schema(description = "身份证号")
    private String idNumber;
    /**
    * 账号状态 1正常 0锁定
    */
    @NotNull(message="[账号状态 1正常 0锁定]不能为空")
    @Schema(description = "账号状态 1正常 0锁定")
    private Integer status;
    /**
    * 创建时间
    */
    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
    * 最后修改时间
    */
    @Schema(description = "最后修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
    * 创建人id
    */
    @Schema(description = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    /**
    * 最后修改人id
    */
    @Schema(description = "最后修改人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


}
