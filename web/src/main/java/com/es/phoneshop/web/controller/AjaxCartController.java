package com.es.phoneshop.web.controller;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.exception.PhoneOutOfStockException;
import com.es.phoneshop.web.controller.pages.dto.AddToCartDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    private static final String WRONG_FORMAT_MESSAGE = "Wrong format";
    private static final String OUT_OF_STOCK_MESSAGE = "Quantity is out of stock";
    @Resource
    private CartService cartService;

    @RequestMapping(method = RequestMethod.POST)
    public String addPhone(@Valid AddToCartDto addToCartDto, BindingResult br,
                           HttpSession session, HttpServletResponse response) {
        if (br.hasErrors()) {
            response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            return WRONG_FORMAT_MESSAGE;
        }
        Cart cart = cartService.getCart(session);
        try {
            cartService.addPhone(cart, addToCartDto.getPhoneId(),
                    addToCartDto.getQuantity());
        } catch (PhoneOutOfStockException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return OUT_OF_STOCK_MESSAGE;
        }
        return cart.getTotalQuantity() + " items " + cart.getTotalCost();
    }
}
