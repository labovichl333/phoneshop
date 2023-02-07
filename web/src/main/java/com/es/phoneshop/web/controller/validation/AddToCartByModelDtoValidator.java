package com.es.phoneshop.web.controller.validation;

import com.es.core.exception.PhoneNotFoundException;
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

            Long validQuantity = processQuantity(curItem.getQuantity(), i, errors);

            String model;

            try {
                model = processModel(curItem.getModel(), validQuantity, i, errors);
            } catch (PhoneNotFoundException e) {
                errors.rejectValue("items[" + i + "].model", "", "Product not found");
                continue;
            }

            curItem.setModel(model);

            if(validQuantity!=null){
                setTrueValidFlag(curItem);
            }



        }
    }

    private Long processQuantity(String quantity, int index, Errors errors) {
        Long forReturn = null;
        quantity = quantity.trim();
        if (isQuantityEmpty(quantity)) {
            errors.rejectValue("items[" + index + "].quantity", "", "Empty quantity");
        } else {
            try {
                forReturn = Long.valueOf(quantity);
            } catch (NumberFormatException e) {
                errors.rejectValue("items[" + index + "].quantity", "", "Not a number");
            }
            if (forReturn != null && forReturn <= 0) {
                forReturn=null;
                errors.rejectValue("items[" + index + "].quantity", "", "Number must be positive");
            }
        }
        return forReturn;

    }

    private String processModel(String model, Long quantity, int index, Errors errors) {
        String forReturn = model.trim();

        if (isModelEmpty(model)) {
            errors.rejectValue("items[" + index + "].model", "", "Empty model");
        } else {
            Phone phone = phoneDao.findPhoneByModel(model)
                    .orElseThrow(PhoneNotFoundException::new);

            if (quantity != null && quantity > 0) {
                long numOfPhones = phoneDao.getInStockQuantity(phone.getId());
                if (quantity > numOfPhones) {
                    errors.rejectValue("items[" + index + "].quantity", "", "Quantity is out of stock");
                }
            }

        }

        return forReturn;
    }


    private boolean isItemEmpty(AddToCartByModelItemDto item) {
        return StringUtils.isBlank(item.getModel()) && StringUtils.isBlank(item.getQuantity());
    }

    private boolean isModelEmpty(String model) {
        return StringUtils.isBlank(model);
    }

    private boolean isQuantityEmpty(String quantity) {
        return StringUtils.isBlank(quantity);
    }

    private void setFalseValidFlag(AddToCartByModelItemDto item) {
        item.setValid(false);
    }

    private void setTrueValidFlag(AddToCartByModelItemDto item) {
        item.setValid(true);
    }




}
