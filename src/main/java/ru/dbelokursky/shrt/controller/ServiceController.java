package ru.dbelokursky.shrt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ServiceController {

    @GetMapping(value = "/404")
    String getPageNotFound() {
        return "notFound";
    }

    @GetMapping(value = "/403")
    String getAccessDeniedPage() {
        return "accessDenied";
    }

    @GetMapping(value = "/index")
    String getIndexPage() {
        return "index";
    }

    @GetMapping(value = "/help")
    String getHelpPage() {
        return "help";
    }
}
