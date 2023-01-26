package com.es.core.cart;

import com.es.core.model.phone.Phone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartItem {
    private final Phone phone;
    private long quantity;
}
