package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.exception.OrderNotFoundException;
import com.es.core.model.order.OrderDao;
import com.es.core.model.order.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping(value = "/admin/orders")
@RequiredArgsConstructor
public class OrdersPageController {
    private static final String ORDERS_ATTRIBUTE = "orders";
    private static final String ORDER_INFO_ATTRIBUTE = "order";

    private static final String ADMIN_ORDERS_PAGE = "adminOrders";

    private static final String ADMIN_ORDER_INFO_PAGE = "adminOrderInfo";

    private static final String FORMATTER_ATTRIBUTE = "formatter";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final OrderDao orderDao;

    @RequestMapping(method = RequestMethod.GET)
    public String getOrders(Model model) {
        model.addAttribute(ORDERS_ATTRIBUTE, orderDao.findAll());
        model.addAttribute(FORMATTER_ATTRIBUTE, formatter);
        return ADMIN_ORDERS_PAGE;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{orderNumber}")
    public String getOrder(Model model, @PathVariable Long orderNumber) {
        model.addAttribute(ORDER_INFO_ATTRIBUTE, orderDao.findById(orderNumber)
                .orElseThrow(OrderNotFoundException::new));
        return ADMIN_ORDER_INFO_PAGE;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/{orderNumber}")
    public String changeOrderStatus(Model model, @PathVariable Long orderNumber, OrderStatus orderStatus) {
        orderDao.updateStatus(orderNumber, orderStatus);
        model.addAttribute(ORDER_INFO_ATTRIBUTE, orderDao.findById(orderNumber)
                .orElseThrow(OrderNotFoundException::new));
        return ADMIN_ORDER_INFO_PAGE;
    }

}
