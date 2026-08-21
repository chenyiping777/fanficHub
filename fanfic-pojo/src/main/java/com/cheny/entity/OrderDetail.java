package com.cheny.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 约稿订单明细表
* @TableName order_detail
*/
@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(name = "OrderDetail", description = "约稿订单明细表")

public class OrderDetail implements Serializable {

    /**
    * 主键
    */
    @NotNull(message="[主键]不能为空")
    @Schema(description = "主键")
    private Long id;
    /**
    * 商品名称
    */
    @Size(max= 32,message="编码长度不能超过32")
    @Schema(description = "商品名称")
    private String name;
    /**
    * 商品图片路径
    */
    @Size(max= 255,message="编码长度不能超过255")
    @Schema(description = "商品图片路径")
    private String image;
    /**
    * 订单id
    */
    @NotNull(message="[订单id]不能为空")
    @Schema(description = "订单id")
    private Long orderId;
    /**
    * 人物id
    */
    @Schema(description = "人物id")
    private Long dishId;
    /**
    * 组合id
    */
    @Schema(description = "组合id")
    private Long setmealId;
    /**
    * 创作规格
    */
    @Size(max= 50,message="编码长度不能超过50")
    @Schema(description = "创作规格")
    private String dishFlavor;
    /**
    * 商品数量
    */
    @Schema(description = "商品数量")
    private Integer number;
    /**
    * 商品单价
    */
    @Schema(description = "商品单价")
    private Double amount;

}
