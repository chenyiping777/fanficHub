package com.cheny.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddressBookVo {


    private Long id;

    /**
     * 收货人
     */
    private String consignee;
    /**
     * 性别
     */
    private String sex;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 省份编码
     */
    private String provinceCode;
    /**
     * 省份名称
     */
    private String provinceName;
    /**
     * 城市编码
     */
    private String cityCode;
    /**
     * 城市名称
     */
    private String cityName;
    /**
     * 区县编码
     */
    private String districtCode;
    /**
     * 区县名称
     */
    private String districtName;
    /**
     * 详细地址信息
     */
    private String detail;
    /**
     * 地址标签（家/公司/学校等）
     */
    private String label;
    /**
     * 是否默认地址 1是 0否
     */
    private Integer isDefault;

}
