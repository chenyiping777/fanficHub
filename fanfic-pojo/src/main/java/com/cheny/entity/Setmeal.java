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
* 明星组合/CP表
* @TableName setmeal
*/
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Setmeal", description = "明星组合/CP表")
@Data
public class Setmeal implements Serializable {

    /**
    * 主键
    */
    @NotNull(message="[主键]不能为空")
    @Schema(description = "主键")
    private Long id;
    /**
    * 组合/CP名称
    */
    @NotBlank(message="[组合/CP名称]不能为空")
    @Size(max= 32,message="编码长度不能超过32")
    @Schema(description = "组合/CP名称")
    private String name;
    /**
    * 分类id
    */
    @NotNull(message="[分类id]不能为空")
    @Schema(description = "分类id")
    private Long categoryId;
    /**
    * 组合约稿打包价格
    */
    @NotNull(message="[组合约稿打包价格]不能为空")
    @Schema(description = "组合约稿打包价格")
    private BigDecimal price;
    /**
    * 组合图片路径
    */
    @Size(max= 255,message="编码长度不能超过255")
    @Schema(description = "组合图片路径")
    private String image;
    /**
    * 组合设定描述
    */
    @Size(max= 255,message="编码长度不能超过255")
    @Schema(description = "组合设定描述")
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
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 最后修改时间
     */
    @Schema(description = "最后修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 创建人id
     */
    @Schema(description = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    /**
     * 最后修改人id
     */
    @Schema(description = "最后修改人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


}
