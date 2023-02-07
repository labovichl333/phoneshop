package com.es.phoneshop.web.controller.pages.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartByModelDto {
    @Valid
    private List<AddToCartByModelItemDto> items;
}
