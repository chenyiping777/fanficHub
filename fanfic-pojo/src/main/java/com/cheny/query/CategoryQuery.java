package com.cheny.query;

import lombok.Data;

@Data
public class CategoryQuery extends PageQuery{
    private Integer type;//1人物  2组合
    private String name;
}
