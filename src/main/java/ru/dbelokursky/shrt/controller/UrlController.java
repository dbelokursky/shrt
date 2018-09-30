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
        String requestUrl = request.getRequestURL().toString();
        String baseUrl = requestUrl.substring(0, requestUrl.lastIndexOf("/") + 1);
        shortUrl.put("shortUrl", String.format("%s%s", baseUrl, savedUrl.getHash()));
        return shortUrl;
    }

    @GetMapping(value = "/{hash}")
    public RedirectView redirect(@PathVariable String hash) {
        RedirectView redirectView = new RedirectView();
        if (!urlService.findByHash(hash).isEmpty()) {
            Url url = urlService.findByHash(hash).iterator().next();
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
