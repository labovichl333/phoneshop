package com.es.phoneshop.web.controller.validation;

import com.es.core.exception.PhoneNotFoundException;
import com.es.core.exception.PhoneOutOfStockException;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.phoneshop.web.controller.pages.dto.AddToCartByModelDto;
import com.es.phoneshop.web.controller.pages.dto.AddToCartByModelItemDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AddToCartByModelDtoValidator implements Validator {

    private final PhoneDao phoneDao;

    @Override
    public boolean supports(Class<?> aClass) {
        return AddToCartByModelDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AddToCartByModelDto addToCartByModelDto = (AddToCartByModelDto) o;
        List<AddToCartByModelItemDto> items = addToCartByModelDto.getItems();
        items.forEach(this::setFalseValidFlag);

        for (int i = 0; i < items.size(); i++) {
            AddToCartByModelItemDto curItem = items.get(i);
            if (isItemEmpty(curItem)) {
                continue;
            }
            String model;
            try {
                model = processModel(curItem, i, errors);
            } catch (PhoneNotFoundException e) {
                errors.rejectValue("items[" + i + "].model", "", "Product not found");
                continue;
            } catch (PhoneOutOfStockException e) {
                errors.rejectValue("items[" + i + "].quantity", "", "Quantity is out of stock");
                continue;
            }
            curItem.setModel(model);

            if (curItem.getQuantity() != null && curItem.getQuantity() > 1) {
                setTrueValidFlag(curItem);
            }

        }
    }


    private String processModel(AddToCartByModelItemDto curItem, int index, Errors errors) {
        String model = curItem.getModel().trim();
        Long quantity = curItem.getQuantity();
        if (isModelEmpty(model)) {
            errors.rejectValue("items[" + index + "].model", "", "Empty model");
        } else {
            Phone phone = phoneDao.findPhoneByModel(model)
                    .orElseThrow(PhoneNotFoundException::new);
            if (quantity != null) {
                long numOfPhones = phoneDao.getInStockQuantity(phone.getId());
                if (quantity > numOfPhones) {
                    throw new PhoneOutOfStockException();
                }
            }
        }
        return model;
    }


    private boolean isItemEmpty(AddToCartByModelItemDto item) {
        return StringUtils.isBlank(item.getModel()) && item.getQuantity() == null;
    }

    private boolean isModelEmpty(String model) {
        return StringUtils.isBlank(model);
    }


    private void setFalseValidFlag(AddToCartByModelItemDto item) {
        item.setValid(false);
    }

    private void setTrueValidFlag(AddToCartByModelItemDto item) {
        item.setValid(true);
    }


}
