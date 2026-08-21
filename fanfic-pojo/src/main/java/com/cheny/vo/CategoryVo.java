package com.cheny.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVo {

    /**
     * 主键
     */

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

    /**
     * 最后修改时间
     */
    private LocalDateTime updateTime;
}
