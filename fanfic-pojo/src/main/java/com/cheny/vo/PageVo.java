package com.cheny.vo;

import lombok.Data;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageVo<T> {
    private Long total;//总条数
    private Long pages;//总页数
    private List<T> list;
}
