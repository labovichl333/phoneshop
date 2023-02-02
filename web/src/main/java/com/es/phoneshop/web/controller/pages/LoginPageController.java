package com.es.phoneshop.web.controller.pages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginPageController {
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String ERROR_MESSAGE = "Wrong name or password";
    private static final String LOGIN_PAGE = "login";

    @RequestMapping(method = RequestMethod.GET)
    public String showLoginPage(@RequestParam(required = false) boolean error, Model model) {
        if (error) {
            model.addAttribute(ERROR_ATTRIBUTE, ERROR_MESSAGE);
        }
        return LOGIN_PAGE;
    }
}