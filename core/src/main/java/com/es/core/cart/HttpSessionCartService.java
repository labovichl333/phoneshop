package com.es.core.cart;

import com.es.core.exception.PhoneNotFoundException;
import com.es.core.exception.PhoneOutOfStockException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HttpSessionCartService implements CartService {

    public static final String CART_SESSION_ATTRIBUTE = "cart";

    private final PhoneDao phoneDao;

    @Override
    public Cart getCart(HttpSession session) {

        synchronized (session) {
            Cart cart = (Cart) session.getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                cart = new Cart();
                session.setAttribute(CART_SESSION_ATTRIBUTE, cart);
            }
            return cart;
        }
    }

    @Override
    public void addPhone(Cart cart, Long phoneId, Long quantity) {
        synchronized (cart) {
            Phone phone = phoneDao.get(phoneId).orElseThrow(PhoneNotFoundException::new);
            Optional<CartItem> optionalCartItem = cart.getItems().stream()
                    .filter(item -> phoneId.equals(item.getPhone().getId()))
                    .findAny();

            long quantityToUpdate = quantity + optionalCartItem
                    .map(CartItem::getQuantity).orElse(0L);

            if (quantityToUpdate > phoneDao.getInStockQuantity(phoneId)) {
                throw new PhoneOutOfStockException();
            }
            if (optionalCartItem.isPresent()) {
                optionalCartItem.get().setQuantity(quantityToUpdate);
            } else {
                cart.getItems().add(new CartItem(phone, quantity));
            }
            recalculateCart(cart);
        }
    }

    @Override
    public void update(Cart cart, Long phoneId, Long quantity) {
        synchronized (cart) {
            CartItem cartItem = cart.getItems().stream()
                    .filter(currentCartItem -> phoneId.equals(currentCartItem.getPhone().getId()))
                    .findAny().get();
            int quantityInStock = phoneDao.getInStockQuantity(phoneId);

            if (quantityInStock >= quantity) {
                cartItem.setQuantity(quantity);
            } else {
                throw new PhoneOutOfStockException();
            }
            recalculateCart(cart);
        }
    }

    @Override
    public void remove(Cart cart, Long phoneId) {
        synchronized (cart) {
            cart.getItems().removeIf(cartItem -> phoneId.equals(cartItem.getPhone().getId()));
            recalculateCart(cart);
        }
    }

    private void recalculateCart(Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
                .map(CartItem::getQuantity)
                .reduce(0L, Long::sum));
        cart.setTotalCost(cart.getItems().stream()
                .map(cartItem -> cartItem.getPhone().getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}
