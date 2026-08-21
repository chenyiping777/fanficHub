package com.cheny.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 人物/组合分类表
* @TableName category
*/
@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(name = "Category", description = "人物/组合分类表")
public class Category implements Serializable {

    /**
    * 主键
    */
    @NotNull(message="[主键]不能为空")
    @Schema(description = "主键")
    private Long id;
    /**
    * 分类名称
    */
    @NotBlank(message="[分类名称]不能为空")
    @Size(max= 32,message="编码长度不能超过32")
    @Schema(description = "分类名称")
    private String name;
    /**
    * 分类类型 1人物分类 2组合分类
    */
    @NotNull(message="[分类类型 1人物分类 2组合分类]不能为空")
    @Schema(description = "分类类型 1人物分类 2组合分类")
    private Integer type;
    /**
    * 排序字段
    */
    @Schema(description = "排序字段")
    private Integer sort;
    /**
    * 状态 1启用 0禁用
    */
    @NotNull(message="[状态 1启用 0禁用]不能为空")
    @Schema(description = "状态 1启用 0禁用")
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
