package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.phoneshop.web.controller.pages.dto.AddToCartRequestDto;
import com.es.phoneshop.web.controller.pages.dto.UpdateCartDto;
import com.es.phoneshop.web.controller.validation.AddToCartRequestDtoValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    @Resource
    private CartService cartService;
    @Resource
    private AddToCartRequestDtoValidator validator;

    @RequestMapping(method = RequestMethod.GET)
    public String getCart(Model model, HttpSession session) {
        Cart cart = cartService.getCart(session);
        model.addAttribute("cart", cart);
        model.addAttribute("cartItems", mapCartItemsToDto(cart));
        return "cart";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateCart(@Valid @ModelAttribute("cartItems") UpdateCartDto updateCartDto,
                             BindingResult br, HttpSession session, Model model) {
        validator.validate(updateCartDto, br);
        if (br.hasErrors()) {
            return "cart";
        }
        Cart cart = cartService.getCart(session);
        updateCartDto.getCartItems()
                .forEach(crtItem -> cartService.update(cart, crtItem.getPhoneId(), Long.valueOf(crtItem.getQuantity())));
        return "redirect:/cart";
    }

    @RequestMapping(value = "/delete/{phoneId}", method = RequestMethod.DELETE)
    public String deleteCartItem(Model model, HttpSession session, @PathVariable Long phoneId) {
        Cart cart = cartService.getCart(session);
        cartService.remove(cart, phoneId);
        model.addAttribute("cartItems", cart.getItems());
        return "redirect:/cart";
    }


    private UpdateCartDto mapCartItemsToDto(Cart cart) {
        List<AddToCartRequestDto> dtoElement = new ArrayList<>();
        cart.getItems().forEach(cartItem -> dtoElement
                .add(new AddToCartRequestDto(cartItem.getPhone().getId(), String.valueOf(cartItem.getQuantity()))));
        UpdateCartDto dto = new UpdateCartDto(dtoElement);
        return dto;
    }

}
