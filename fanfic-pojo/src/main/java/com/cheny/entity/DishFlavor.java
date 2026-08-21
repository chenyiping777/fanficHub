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
* 人物创作规格表
* @TableName dish_flavor
*/
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "DishFlavor", description = "人物创作规格表")
@Data
public class DishFlavor implements Serializable {

    /**
    * 主键
    */
    @NotNull(message="[主键]不能为空")
    @Schema(description = "主键")
    private Long id;
    /**
    * 人物id
    */
    @NotNull(message="[人物id]不能为空")
    @Schema(description = "人物id")
    private Long dishId;
    /**
    * 规格维度（世界观/字数/文风等）
    */
    @Size(max= 32,message="编码长度不能超过32")
    @Schema(description = "规格维度（世界观/字数/文风等）")
    private String name;
    /**
    * 规格可选值（JSON数组格式）
    */
    @Size(max= 255,message="编码长度不能超过255")
    @Schema(description = "规格可选值（JSON数组格式）")
    private String value;



}
