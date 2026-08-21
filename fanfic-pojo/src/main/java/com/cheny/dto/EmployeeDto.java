package com.cheny.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeDto {

    //性别用户名不能为空

    //id,idNumber,name,phone        ,sex,username
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
    @Schema(description = "用户名")
    private String username;
    /**
     * 手机号
     */
    @Size(max= 11,message="编码长度不能超过11")
    @Schema(description = "手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
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

    @Schema(description = "密码，默认值为：abc123")
    //正则校验，密码长度在6到20之间，必须包含一个字母
    @Pattern(regexp="^(?=.*[a-zA-Z]).{6,20}$",message="密码长度在6到20之间，必须包含一个字母")
    private String password = "abc123";
}
