package com.es.core.order;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderDao;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.PhoneDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:conf/application.properties")
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;

    private final PhoneDao phoneDao;

    private final CartService cartService;

    @Value("${delivery.price}")
    private BigDecimal deliveryPrice;

    @Override
    public Order createOrder(Cart cart) {
        synchronized (cart) {
            Order order = new Order();
            calculatePrice(order, cart);
            List<OrderItem> orderItems = cart.getItems().stream()
                    .map(cartItem -> new OrderItem(cartItem.getPhone(), order, cartItem.getQuantity()))
                    .collect(Collectors.toList());
            order.setOrderItems(orderItems);
            return order;
        }
    }

    @Override
    @Transactional(rollbackFor = OutOfStockException.class)
    public void placeOrder(Order order, HttpSession httpSession) throws OutOfStockException {
        Cart cart = cartService.getCart(httpSession);
        synchronized (cart) {
            long startCartQuantity = cart.getTotalQuantity();

            order.getOrderItems().forEach(orderItem -> {
                long realPhoneQuantity = phoneDao.getInStockQuantity(orderItem.getPhone().getId());
                if (realPhoneQuantity < orderItem.getQuantity()) {
                    cartService.update(cart, orderItem.getPhone().getId(), realPhoneQuantity);
                }
            });
            long finalCartQuantity = cart.getTotalQuantity();
            if (finalCartQuantity < startCartQuantity) {
                throw new OutOfStockException();
            }
            order.setStatus(OrderStatus.NEW);
            order.setSecureId(UUID.randomUUID().toString());
            order.setCreatedDate(LocalDateTime.now());
            orderDao.save(order);
            cartService.clearCart(httpSession);
        }

    }

    private void calculatePrice(Order order, Cart cart) {
        order.setDeliveryPrice(deliveryPrice);
        order.setSubtotal(cart.getTotalCost());
        order.setTotalPrice(deliveryPrice.add(cart.getTotalCost()));
    }

}
