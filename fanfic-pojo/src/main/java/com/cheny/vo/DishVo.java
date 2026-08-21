package com.cheny.vo;

import com.cheny.entity.DishFlavor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishVo {

    private Long id;

    private String name;

    private Long categoryId;

    private Double price;

    private String image;

    private String description;

    private Integer status;

    private LocalDateTime updateTime;

    private List<DishFlavor> flavors = new ArrayList<>();

}
