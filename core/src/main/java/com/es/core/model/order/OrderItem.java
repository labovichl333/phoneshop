package com.es.core.model.order;

import com.es.core.model.phone.Phone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private Long id;
    private Phone phone;
    private Order order;
    private Long quantity;

    public OrderItem(Phone phone, Order order, Long quantity) {
        this.phone = phone;
        this.order = order;
        this.quantity = quantity;
    }
}
