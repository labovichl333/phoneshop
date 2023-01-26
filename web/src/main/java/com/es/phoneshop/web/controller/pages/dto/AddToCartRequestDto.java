package com.es.phoneshop.web.controller.pages.dto;

import lombok.*;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartRequestDto {
    private Long phoneId;

    @Min(1)
    private Long quantity;
}
