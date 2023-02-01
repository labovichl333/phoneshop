package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.phoneshop.web.controller.pages.dto.AddToCartRequestDto;
import com.es.phoneshop.web.controller.pages.dto.UpdateCartDto;
import com.es.phoneshop.web.controller.validation.AddToCartRequestDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/cart")
@RequiredArgsConstructor
public class CartPageController {

    private static final String CART_ATTRIBUTE="cart";

    private static final String CART_ITEMS_ATTRIBUTE="cartItems";

    private static final String CART_PAGE="cart";

    private static final String REDIRECT_TO_CART="redirect:/cart";


    private final CartService cartService;

    private final AddToCartRequestDtoValidator validator;

    @RequestMapping(method = RequestMethod.GET)
    public String getCart(Model model, HttpSession session) {
        Cart cart = cartService.getCart(session);
        model.addAttribute(CART_ATTRIBUTE, cart);
        model.addAttribute(CART_ITEMS_ATTRIBUTE, mapCartItemsToDto(cart));
        return CART_PAGE;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateCart(@Valid @ModelAttribute("cartItems") UpdateCartDto updateCartDto,
                             BindingResult br, HttpSession session) {
        validator.validate(updateCartDto, br);
        if (br.hasErrors()) {
            return CART_PAGE;
        }
        Cart cart = cartService.getCart(session);
        updateCartDto.getCartItems()
                .forEach(crtItem -> cartService.update(cart, crtItem.getPhoneId(), Long.valueOf(crtItem.getQuantity())));
        return REDIRECT_TO_CART;
    }

    @RequestMapping(value = "/delete/{phoneId}", method = RequestMethod.DELETE)
    public String deleteCartItem(Model model, HttpSession session, @PathVariable Long phoneId) {
        Cart cart = cartService.getCart(session);
        cartService.remove(cart, phoneId);
        model.addAttribute(CART_ITEMS_ATTRIBUTE, cart.getItems());
        return REDIRECT_TO_CART;
    }


    private UpdateCartDto mapCartItemsToDto(Cart cart) {
        List<AddToCartRequestDto> dtoElement = new ArrayList<>();
        cart.getItems().forEach(cartItem -> dtoElement
                .add(new AddToCartRequestDto(cartItem.getPhone().getId(), String.valueOf(cartItem.getQuantity()))));
        UpdateCartDto dto = new UpdateCartDto(dtoElement);
        return dto;
    }


}
