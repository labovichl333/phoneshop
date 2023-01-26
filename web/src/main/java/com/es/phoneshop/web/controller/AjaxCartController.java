package com.es.phoneshop.web.controller;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.exception.PhoneOutOfStockException;
import com.es.phoneshop.web.controller.pages.dto.AddToCartRequestDto;
import com.es.phoneshop.web.controller.pages.dto.AddToCartResponseDto;
import com.es.phoneshop.web.controller.pages.dto.ExeptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
    public ResponseEntity<?> addPhone(@Valid AddToCartRequestDto addToCartRequestDto, BindingResult br,
                                      HttpSession session) {
        if (br.hasErrors()) {
            return new ResponseEntity<>(new ExeptionDto(WRONG_FORMAT_MESSAGE), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Cart cart = cartService.getCart(session);
        try {
            cartService.addPhone(cart, addToCartRequestDto.getPhoneId(),
                    addToCartRequestDto.getQuantity());
        } catch (PhoneOutOfStockException e) {
            return new ResponseEntity<>(new ExeptionDto(OUT_OF_STOCK_MESSAGE), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(mapCartToDto(cart), HttpStatus.OK);
    }


    private AddToCartResponseDto mapCartToDto(Cart cart){
        return AddToCartResponseDto.builder()
                .totalCost(cart.getTotalCost())
                .totalQuantity(cart.getTotalQuantity())
                .build();
    }
}
