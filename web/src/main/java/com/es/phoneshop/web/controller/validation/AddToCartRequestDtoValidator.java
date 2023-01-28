package com.es.phoneshop.web.controller.validation;

import com.es.core.model.phone.PhoneDao;
import com.es.phoneshop.web.controller.pages.dto.AddToCartRequestDto;
import com.es.phoneshop.web.controller.pages.dto.UpdateCartDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AddToCartRequestDtoValidator implements Validator {

    private final PhoneDao phoneDao;

    @Override
    public boolean supports(Class<?> aClass) {
        return AddToCartRequestDtoValidator.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UpdateCartDto dto = (UpdateCartDto) o;
        List<AddToCartRequestDto> values = dto.getCartItems();
        for (int i = 0; i < values.size(); i++) {
            AddToCartRequestDto curVal = values.get(i);
            long quantity;
            try {
                quantity = Long.parseLong(curVal.getQuantity());
            } catch (NumberFormatException e) {
                errors.rejectValue("cartItems[" + i + "].quantity", "notANumber", "not a natural number");
                continue;
            }

            if (quantity < 1) {
                errors.rejectValue("cartItems[" + i + "].quantity", "lessThenOne", "quantity less than one");
            }

            if (quantity > phoneDao.getInStockQuantity(curVal.getPhoneId())) {
                errors.rejectValue("cartItems[" + i + "].quantity", "outOfStock", "out of stock");
            }


        }
    }
}
