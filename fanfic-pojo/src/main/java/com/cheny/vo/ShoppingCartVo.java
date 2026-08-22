package com.cheny.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartVo {
    /**
     * 主键
     */
    private Long id;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品图片路径
     */
    private String image;
    /**
     * 人物id
     */
    private Long dishId;
    /**
     * 组合id
     */
    private Long setmealId;
    /**
     * 选中的创作规格
     */
    private String dishFlavor;
    /**
     * 商品数量
     */
    private Integer number;
    /**
     * 商品单价
     */
    private Double amount;
}
