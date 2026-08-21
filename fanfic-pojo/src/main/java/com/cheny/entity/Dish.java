package com.cheny.entity;

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
* 明星人物表
* @TableName dish
*/
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Dish", description = "明星人物表")
@Data
public class Dish implements Serializable {

    /**
    * 主键
    */
    @NotNull(message="[主键]不能为空")
    @Schema(description = "主键")
    private Long id;
    /**
    * 人物名称
    */
    @NotBlank(message="[人物名称]不能为空")
    @Size(max= 32,message="编码长度不能超过32")
    @Schema(description = "人物名称")
    private String name;
    /**
    * 分类id
    */
    @NotNull(message="[分类id]不能为空")
    @Schema(description = "分类id")
    private Long categoryId;
    /**
    * 单人约稿基础价格
    */
    @NotNull(message="[单人约稿基础价格]不能为空")
    @Schema(description = "单人约稿基础价格")
    private Double price;
    /**
    * 人物图片路径
    */
    @Size(max= 255,message="编码长度不能超过255")
    @Schema(description = "人物图片路径")
    private String image;
    /**
    * 人物描述
    */
    @Size(max= 255,message="编码长度不能超过255")
    @Schema(description = "人物描述")
    private String description;
    /**
    * 上架状态 1起售 0停售
    */
    @NotNull(message="[上架状态 1起售 0停售]不能为空")
    @Schema(description = "上架状态 1起售 0停售")
    private Integer status;
    /**
    * 创建时间
    */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    /**
    * 最后修改时间
    */
    @Schema(description = "最后修改时间")
    private LocalDateTime updateTime;
    /**
    * 创建人id
    */
    @Schema(description = "创建人id")
    private Long createUser;
    /**
    * 最后修改人id
    */
    @Schema(description = "最后修改人id")
    private Long updateUser;


}
