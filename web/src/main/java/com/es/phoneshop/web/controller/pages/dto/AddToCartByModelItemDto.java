package com.es.phoneshop.web.controller.pages.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartByModelItemDto {
    private String model;
    private String quantity;
    private boolean isValid;
}
