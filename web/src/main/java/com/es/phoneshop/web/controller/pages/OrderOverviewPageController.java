package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.OrderNotFoundException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/orderOverview")
@RequiredArgsConstructor
public class OrderOverviewPageController {

    private static final String ORDER_ATTRIBUTE = "order";
    private static final String ORDER_OVERVIEW_PAGE = "orderOverview";
    private final OrderDao orderDao;

    @RequestMapping(value = "/{secureId}", method = RequestMethod.GET)
    public String getOrder(Model model, @PathVariable String secureId) {
        Order order = orderDao.findBySecureId(secureId).orElseThrow(OrderNotFoundException::new);
        model.addAttribute(ORDER_ATTRIBUTE, order);
        return ORDER_OVERVIEW_PAGE;
    }
}
