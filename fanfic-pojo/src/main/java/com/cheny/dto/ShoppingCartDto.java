package com.cheny.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDto {
    private Long dishId;
    private Long setmealId;
    private String dishFlavor;
}
