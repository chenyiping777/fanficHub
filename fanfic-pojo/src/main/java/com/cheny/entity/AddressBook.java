package com.cheny.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 用户交付地址表
* @TableName address_book
*/
@NoArgsConstructor
@AllArgsConstructor

@Data
public class AddressBook implements Serializable {

    /**
    * 主键
    */
    @NotNull(message="[主键]不能为空")
    @Schema(description = "主键")
    private Long id;
    /**
    * 用户id
    */
    @NotNull(message="[用户id]不能为空")
    @Schema(description = "用户id")
    private Long userId;
    /**
    * 收货人
    */
    @Size(max= 50,message="编码长度不能超过50")
    @Schema(description = "收货人")
    private String consignee;
    /**
    * 性别
    */
    @Size(max= 2,message="编码长度不能超过2")
    @Schema(description = "性别")
    private String sex;
    /**
    * 手机号
    */
    @Size(max= 11,message="编码长度不能超过11")
    @Schema(description = "手机号")
    private String phone;
    /**
    * 省份编码
    */
    @Size(max= 12,message="编码长度不能超过12")
    @Schema(description = "省份编码")
    private String provinceCode;
    /**
    * 省份名称
    */
    @Size(max= 32,message="编码长度不能超过32")
    @Schema(description = "省份名称")
    private String provinceName;
    /**
    * 城市编码
    */
    @Size(max= 12,message="编码长度不能超过12")
    @Schema(description = "城市编码")
    private String cityCode;
    /**
    * 城市名称
    */
    @Size(max= 32,message="编码长度不能超过32")
    @Schema(description = "城市名称")
    private String cityName;
    /**
    * 区县编码
    */
    @Size(max= 12,message="编码长度不能超过12")
    @Schema(description = "区县编码")
    private String districtCode;
    /**
    * 区县名称
    */
    @Size(max= 32,message="编码长度不能超过32")
    @Schema(description = "区县名称")
    private String districtName;
    /**
    * 详细地址信息
    */
    @Size(max= 200,message="编码长度不能超过200")
    @Schema(description = "详细地址信息")
    private String detail;
    /**
    * 地址标签（家/公司/学校等）
    */
    @Size(max= 100,message="编码长度不能超过100")
    @Schema(description = "地址标签（家/公司/学校等）")
    private String label;
    /**
    * 是否默认地址 1是 0否
    */
    @Schema(description = "是否默认地址 1是 0否")
    private Integer isDefault;

}
