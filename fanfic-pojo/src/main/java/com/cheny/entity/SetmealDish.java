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
* 组合人物关联表
* @TableName setmeal_dish
*/
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SetmealDish", description = "组合人物关联表")
@Data
public class SetmealDish implements Serializable {

    /**
    * 主键
    */
    @NotNull(message="[主键]不能为空")
    @Schema(description = "主键")
    private Long id;
    /**
    * 组合id
    */
    @NotNull(message="[组合id]不能为空")
    @Schema(description = "组合id")
    private Long setmealId;
    /**
    * 人物id
    */
    @NotNull(message="[人物id]不能为空")
    @Schema(description = "人物id")
    private Long dishId;
    /**
    * 人物名称（冗余字段）
    */
    @Size(max= 32,message="编码长度不能超过32")
    @Schema(description = "人物名称（冗余字段）")
    private String name;
    /**
    * 人物单价（冗余字段）
    */
    @Schema(description = "人物单价（冗余字段）")
    private BigDecimal price;
    /**
    * 人物戏份份数
    */
    @Schema(description = "人物戏份份数")
    private Integer copies;



}
