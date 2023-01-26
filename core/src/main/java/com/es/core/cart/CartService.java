package com.es.core.cart;

import javax.servlet.http.HttpSession;

public interface CartService {

    Cart getCart(HttpSession session);

    void addPhone(Cart cart, Long phoneId, Long quantity);

    /**
     * @param items key: {@link com.es.core.model.phone.Phone#id}
     *              value: quantity
     */
    void update(Cart cart, Long phoneId, Long quantity);

    void remove(Cart cart, Long phoneId);

}
