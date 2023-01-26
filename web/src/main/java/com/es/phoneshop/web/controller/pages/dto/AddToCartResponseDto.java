package com.es.phoneshop.web.controller.pages.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class AddToCartResponseDto implements Serializable {
    private BigDecimal totalCost;
    private Long totalQuantity;
}
