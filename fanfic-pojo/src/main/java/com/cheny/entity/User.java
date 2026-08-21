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
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


/**
* C端约稿用户表
* @TableName user
*/
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "User", description = "C端约稿用户表")
@Data
public class User implements Serializable {

    /**
    * 主键
    */
    @NotNull(message="[主键]不能为空")
    @Schema(description = "主键")
    private Long id;
    /**
    * 微信用户唯一标识
    */
    @Size(max= 45,message="编码长度不能超过45")
    @Schema(description = "微信用户唯一标识")
    private String openid;
    /**
    * 用户姓名
    */
    @Size(max= 32,message="编码长度不能超过32")
    @Schema(description = "用户姓名")
    private String name;
    /**
    * 手机号
    */
    @Size(max= 11,message="编码长度不能超过11")
    @Schema(description = "手机号")
    private String phone;
    /**
    * 性别
    */
    @Size(max= 2,message="编码长度不能超过2")
    @Schema(description = "性别")
    private String sex;
    /**
    * 身份证号
    */
    @Size(max= 18,message="编码长度不能超过18")
    @Schema(description = "身份证号")
    private String idNumber;
    /**
    * 用户头像路径
    */
    @Size(max= 500,message="编码长度不能超过500")
    @Schema(description = "用户头像路径")
    private String avatar;
    /**
    * 注册时间
    */
    @Schema(description = "注册时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
