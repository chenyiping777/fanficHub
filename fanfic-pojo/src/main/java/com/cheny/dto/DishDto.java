package com.cheny.dto;

import com.cheny.entity.DishFlavor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DishDto {
    //主键，名称，价格，分类，状态和图片路径都不能为空 描述可以
    /**
     * 主键
     */
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
    private Long categoryId;//下拉框选择
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
    @NotNull(message = "菜品的图片不能为空")
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

    private List<DishFlavor> flavorsList;
}
