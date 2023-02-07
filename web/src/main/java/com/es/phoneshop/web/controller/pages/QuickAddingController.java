package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.model.phone.PhoneDao;
import com.es.phoneshop.web.controller.pages.dto.AddToCartByModelDto;
import com.es.phoneshop.web.controller.pages.dto.AddToCartByModelItemDto;
import com.es.phoneshop.web.controller.validation.AddToCartByModelDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/quickAdd")
@RequiredArgsConstructor
public class QuickAddingController {

    private final CartService cartService;

    private final PhoneDao phoneDao;

    private final AddToCartByModelDtoValidator validator;


    @RequestMapping(method = RequestMethod.GET)
    public String showQuickAddingPage(Model model) {
        model.addAttribute("items", new AddToCartByModelDto());
        return "quickAdd";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addPhonesByModels(@ModelAttribute("items") @Valid AddToCartByModelDto addToCartByModelDto,
                                    BindingResult br, Model model, HttpSession session) {
        validator.validate(addToCartByModelDto, br);
        if (br.hasErrors()) {
            model.addAttribute("errorsMessage", "There were errors");
        } else {
            model.addAttribute("allSuccessMessage", "All products added successfully");
        }
        Cart cart = cartService.getCart(session);
        List<String> productsWasAdd = new ArrayList<>();
        addToCartByModelDto.getItems().stream()
                .filter(AddToCartByModelItemDto::isValid)
                .forEach(itemDto -> {
                    cartService.addPhone(cart, phoneDao.findPhoneByModel(itemDto.getModel()).get().getId(),
                            itemDto.getQuantity());
                    productsWasAdd.add(itemDto.getModel() + " products added successfully");
                    clearDtoItem(itemDto);
                });

        model.addAttribute("productsWasAdd", productsWasAdd);

        return "quickAdd";
    }

    private void clearDtoItem(AddToCartByModelItemDto itemDto) {
        itemDto.setModel("");
        itemDto.setQuantity(null);
    }

}
