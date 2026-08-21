package com.cheny.vo;

import com.cheny.entity.SetmealDish;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SetmealVo {


    private Long id;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 分类类型 1人物分类 2组合分类
     */
    private Integer type;
    /**
     * 排序字段
     */
    private Integer sort;
    /**
     * 状态 1启用 0禁用
     */
    private Integer status;

    private List<SetmealDish> setmealDishes;

    private LocalDateTime updateTime;
}
