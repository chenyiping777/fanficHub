package com.cheny.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.Date;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 约稿订单主表
* @TableName orders
*/
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Orders")
@Data
public class Orders implements Serializable {

    /**
    * 主键
    */
    @NotNull(message="[主键]不能为空")
    @Schema(description = "主键")
    private Long id;
    /**
    * 订单号
    */
    @NotBlank(message="[订单号]不能为空")
    @Size(max= 50,message="编码长度不能超过50")
    @Schema(description = "订单号")
    private String number;
    /**
    * 订单状态 1待付款 2待接单 3创作中 4交付中 5已完成 6已取消
    */
    @NotNull(message="[订单状态 1待付款 2待接单 3创作中 4交付中 5已完成 6已取消]不能为空")
    @Schema(description = "订单状态 1待付款 2待接单 3创作中 4交付中 5已完成 6已取消")
    private Integer status;
    /**
    * 用户id
    */
    @NotNull(message="[用户id]不能为空")
    @Schema(description = "用户id")
    private Long userId;
    /**
    * 地址id
    */
    @NotNull(message="[地址id]不能为空")
    @Schema(description = "地址id")
    private Long addressBookId;
    /**
    * 下单时间
    */
    @Schema(description = "下单时间")
    private LocalDateTime orderTime;
    /**
    * 付款时间
    */
    @Schema(description = "付款时间")
    private LocalDateTime checkoutTime;
    /**
    * 支付方式 1微信支付 2支付宝支付
    */
    @Schema(description = "支付方式 1微信支付 2支付宝支付")
    private Integer payMethod;
    /**
    * 支付状态 0未支付 1已支付 2退款
    */
    @Schema(description = "支付状态 0未支付 1已支付 2退款")
    private Integer payStatus;
    /**
    * 订单总金额
    */
    @NotNull(message="[订单总金额]不能为空")
    @Schema(description = "订单总金额")
    private Double amount;
    /**
    * 约稿备注信息
    */
    @Size(max= 100,message="编码长度不能超过100")
    @Schema(description = "约稿备注信息")
    private String remark;
    /**
    * 联系手机号
    */
    @Size(max= 11,message="编码长度不能超过11")
    @Schema(description = "联系手机号")
    private String phone;
    /**
    * 详细地址信息
    */
    @Size(max= 255,message="编码长度不能超过255")
    @Schema(description = "详细地址信息")
    private String address;
    /**
    * 用户姓名
    */
    @Size(max= 32,message="编码长度不能超过32")
    @Schema(description = "用户姓名")
    private String userName;
    /**
    * 收货人
    */
    @Size(max= 32,message="编码长度不能超过32")
    @Schema(description = "收货人")
    private String consignee;
    /**
    * 订单取消原因
    */
    @Size(max= 255,message="编码长度不能超过255")
    @Schema(description = "订单取消原因")
    private String cancelReason;
    /**
    * 拒单原因
    */
    @Size(max= 255,message="编码长度不能超过255")
    @Schema(description = "拒单原因")
    private String rejectionReason;
    /**
    * 订单取消时间
    */
    @Schema(description = "订单取消时间")
    private LocalDateTime cancelTime;
    /**
    * 预计交付时间
    */
    @Schema(description = "预计交付时间")
    private LocalDateTime estimatedDeliveryTime;
    /**
    * 交付状态 1立即交付 0选择具体时间
    */
    @Schema(description = "交付状态 1立即交付 0选择具体时间")
    private Integer deliveryStatus;
    /**
    * 实际交付时间
    */
    @Schema(description = "实际交付时间")
    private LocalDateTime deliveryTime;
    /**
    * 打包费
    */
    @Schema(description = "打包费")
    private Integer packAmount;
    /**
    * 周边份数
    */
    @Schema(description = "周边份数")
    private Integer tablewareNumber;
    /**
    * 周边数量状态 1按稿量提供 0选择具体数量
    */
    @Schema(description = "周边数量状态 1按稿量提供 0选择具体数量")
    private Integer tablewareStatus;

}
