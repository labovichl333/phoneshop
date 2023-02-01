package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.model.order.Order;
import com.es.core.order.OrderService;
import com.es.core.order.OutOfStockException;
import com.es.phoneshop.web.controller.pages.dto.AddToCartRequestDto;
import com.es.phoneshop.web.controller.pages.dto.UpdateCartDto;
import com.es.phoneshop.web.controller.pages.util.Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/order")
@RequiredArgsConstructor
public class OrderPageController {

    private static final String ERROE_MESSAGE_ATRIBUTE = "error_message";
    private static final String ERROE_MESSAGE = "Not enough phones in stock, cart has been updated for valid values";
    private static final String CART_ATRIBUTE = "cart";
    private static final String CART_ITEMS_ATRIBUTE = "cartItems";
    private static final String CART_PAGE = "cart";
    private static final String REDIRECT_TO_ORDER_OVERVIEW = "redirect:/orderOverview/";
    private final OrderService orderService;
    private final CartService cartService;

    @RequestMapping(method = RequestMethod.GET)
    public void getOrder() {

    }

    @RequestMapping(method = RequestMethod.POST)
    public String placeOrder(Model model, HttpSession httpSession) {
        Cart cart = cartService.getCart(httpSession);
        Order order = orderService.createOrder(cart);
        generateOrderInfo(order);
        try {
            orderService.placeOrder(order, httpSession);
        } catch (OutOfStockException e) {
            model.addAttribute(ERROE_MESSAGE_ATRIBUTE, ERROE_MESSAGE);
            model.addAttribute(CART_ATRIBUTE, cart);
            model.addAttribute(CART_ITEMS_ATRIBUTE, mapCartItemsToDto(cart));
            return CART_PAGE;
        }

        return REDIRECT_TO_ORDER_OVERVIEW + order.getSecureId();
    }

    private UpdateCartDto mapCartItemsToDto(Cart cart) {
        List<AddToCartRequestDto> dtoElement = new ArrayList<>();
        cart.getItems().forEach(cartItem -> dtoElement
                .add(new AddToCartRequestDto(cartItem.getPhone().getId(), String.valueOf(cartItem.getQuantity()))));
        UpdateCartDto dto = new UpdateCartDto(dtoElement);
        return dto;
    }

    private void generateOrderInfo(Order order) {
        order.setFirstName(Generator.generateRandomString(10));
        order.setLastName(Generator.generateRandomString(10));
        order.setDeliveryAddress(Generator.generateRandomString(50));
        order.setAdditionalInformation(Generator.generateRandomString(150));
        order.setContactPhoneNo(Generator.generateRandomPhone());
    }


}
