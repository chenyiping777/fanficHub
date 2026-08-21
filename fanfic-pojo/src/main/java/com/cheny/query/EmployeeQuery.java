package com.cheny.query;

import lombok.Data;

@Data
public class EmployeeQuery extends PageQuery {
    private String name;//输入员工姓名查找
}
