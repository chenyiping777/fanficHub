package com.cheny.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryDto {
    /**
     * 主键
     */
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


}
