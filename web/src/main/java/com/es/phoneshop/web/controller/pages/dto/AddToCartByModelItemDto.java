package com.es.phoneshop.web.controller.pages.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartByModelItemDto {
    @Size(max = 50, message = "Max Model name length 50 characters")
    private String model;
    @Min(value = 1, message = "Number must be positive")
    private Long quantity;
    private boolean isValid;
}
