package ru.dbelokursky.shrt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.dbelokursky.shrt.domain.Url;
import ru.dbelokursky.shrt.service.UrlService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UrlController {

    private final UrlService urlService;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping(value = "/register", produces = "application/json", consumes = "application/json")
    public Map<String, String> register(@RequestBody Url url, HttpServletRequest request) {
        Map<String, String> shortUrl = new HashMap<>();
        Url savedUrl = urlService.save(url);
        shortUrl.put("shortUrl", String.format("%s%s%s", request.getRequestURL(), "/", savedUrl.getHash()));
        return shortUrl;
    }

    @GetMapping(value = "/{hash}")
    public RedirectView redirect(@PathVariable String hash) {
        RedirectView redirectView = new RedirectView();
        if (urlService.findByHash(hash).isPresent()) {
            Url url = urlService.findByHash(hash).get();
            redirectView.setUrl(url.getUrl());
            redirectView.setStatusCode(HttpStatus.valueOf(url.getRedirectCode()));
            urlService.incrementCounter(url);
        } else {
            redirectView.setStatusCode(HttpStatus.NOT_FOUND);
            redirectView.setUrl("/404");
        }
        return redirectView;
    }
}
