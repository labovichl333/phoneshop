package com.es.phoneshop.web.controller.pages.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartDto {
    private Long phoneId;

    @Min(1)
    private Long quantity;
}
