package com.cheny.dto;

import com.cheny.entity.SetmealDish;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SetmealDto {

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

    @NotNull(message = "套餐至少选一个菜品")
    private List<SetmealDish> setmealDishes;
}
