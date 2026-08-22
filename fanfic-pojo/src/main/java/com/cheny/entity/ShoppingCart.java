package com.cheny.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 约稿购物车表
* @TableName shopping_cart
*/
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ShoppingCart", description = "约稿购物车表")
@Data
public class ShoppingCart implements Serializable {

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
    @Schema(description = "商品名称")//商品名称，图片路径String，单价double，数量
    private String name;
    /**
    * 商品图片路径
    */
    @Size(max= 255,message="编码长度不能超过255")
    @Schema(description = "商品图片路径")
    private String image;
    /**
    * 用户id
    */
    @NotNull(message="[用户id]不能为空")
    @Schema(description = "用户id")
    private Long userId;
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
    * 选中的创作规格
    */
    @Size(max= 50,message="编码长度不能超过50")
    @Schema(description = "选中的创作规格")
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
    /**
    * 创建时间
    */
    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
