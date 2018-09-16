package ru.dbelokursky.shrt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String getIndexPage(Model model) {
        model.addAttribute("url", new Url());
        return "index";
    }

    @PostMapping("/add")
    public String addUrl(@ModelAttribute Url url) {
        urlService.save(url);
        return "/index";
    }
}
