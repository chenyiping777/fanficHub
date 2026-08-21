package com.cheny.query;

import lombok.Data;
@Data
public class PageQuery {
    //默认第一页
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    private String sortBy;
    private Boolean asc;
}
