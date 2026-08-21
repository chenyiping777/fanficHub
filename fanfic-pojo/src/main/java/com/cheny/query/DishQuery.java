package com.cheny.query;


import lombok.Data;

@Data
public class DishQuery extends PageQuery{
    //菜品名称，菜品分类，售卖状态
    private String name;
    private Integer categoryId;
    private Integer status;
}
