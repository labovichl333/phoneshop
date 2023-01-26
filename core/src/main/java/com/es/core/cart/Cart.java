package com.es.core.cart;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class Cart {
    private final List<CartItem> items = new ArrayList<>();
    private BigDecimal totalCost = BigDecimal.ZERO;
    private long totalQuantity;
}
