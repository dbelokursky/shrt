package ru.dbelokursky.shrt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.dbelokursky.shrt.domain.Url;
import ru.dbelokursky.shrt.service.UrlService;

@Controller
public class UrlController {

    private final UrlService urlService;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/")
    public String getIndexPage() {
        return "index";
    }

    @PostMapping("/add")
    public String addUrl(@ModelAttribute Url url) {
        return "/index";
    }
}
