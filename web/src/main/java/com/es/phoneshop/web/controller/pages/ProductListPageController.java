package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.CartService;
import com.es.core.cart.HttpSessionCartService;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.PhoneDao;
import com.es.core.model.phone.SortField;
import com.es.core.model.phone.SortOrder;
import com.es.phoneshop.web.controller.pagination.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/productList")
public class ProductListPageController {
    private static final int MAX_PAGES_TO_DISPLAY_SIMULTANEOSLY = 9;
    private static final int PHONES_ON_ONE_PAGE_LIMIT = 10;

    private static final String PHONES_INFO_ATTRIBUTE = "phones";

    private static final String PAGES_INFO_ATTRIBUTE = "pages";

    private static final String LAST_PAGE_ATTRIBUTE = "lastPage";

    private static final String PAGE_NUMBER_ATTRIBUTE = "pageNumber";

    private static final String PRODUCT_LIST_PAGE = "productList";

    private final PhoneDao phoneDao;

    private final CartService cartService;

    @RequestMapping(method = RequestMethod.GET, path = "/{pageNumber}")
    public String showProductList(Model model, @PathVariable int pageNumber,
                                  @RequestParam(required = false) String query,
                                  @RequestParam(required = false) SortField sortField,
                                  @RequestParam(required = false) SortOrder sortOrder,
                                  HttpSession session) {
        int offset = PHONES_ON_ONE_PAGE_LIMIT * (pageNumber - 1);
        List<Phone> phonesInfo = phoneDao.findPhonesInStock(query, sortField,
                sortOrder, offset, PHONES_ON_ONE_PAGE_LIMIT);
        long quantityOfPhonesInStock = phoneDao.getQuantityOfPhonesInStock(query);
        int lastPageNumber = (int) Math.ceil((double) quantityOfPhonesInStock / PHONES_ON_ONE_PAGE_LIMIT);
        model.addAttribute(PHONES_INFO_ATTRIBUTE, phonesInfo);
        model.addAttribute(PAGES_INFO_ATTRIBUTE, Pagination.calculatePageNumbers(pageNumber, lastPageNumber,
                MAX_PAGES_TO_DISPLAY_SIMULTANEOSLY));
        model.addAttribute(LAST_PAGE_ATTRIBUTE, lastPageNumber);
        model.addAttribute(PAGE_NUMBER_ATTRIBUTE, pageNumber);
        model.addAttribute(HttpSessionCartService.CART_SESSION_ATTRIBUTE, cartService.getCart(session));
        return PRODUCT_LIST_PAGE;
    }

}
