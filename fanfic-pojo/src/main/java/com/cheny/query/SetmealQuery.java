package com.cheny.query;

import lombok.Data;

@Data
public class SetmealQuery extends PageQuery{
    private String name;
    private Integer status;//状态
    private Long categoryId;

}
