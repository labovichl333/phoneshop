package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.model.order.Order;

import javax.servlet.http.HttpSession;

public interface OrderService {
    Order createOrder(Cart cart);

    void placeOrder(Order order, HttpSession session) throws OutOfStockException;

}
