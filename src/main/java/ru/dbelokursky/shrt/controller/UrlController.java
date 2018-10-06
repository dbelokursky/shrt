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

    /**
     * Request:
     * JSON object with the following parameters:
     * url (mandatory, url that needs shortening)
     * redirectType : 301 | 302 (not mandatory, default 302)
     * Example: {
     * "url": "http://stackoverflow.com/questions/1567929/website-safe-data-
     * access-architecture-question?rq=1",
     * "redirectType": "301"
     * }
     *
     * Response:
     * Response parameters in case of successful registration are as follows:
     * shortUrl (shortened URL)
     * Example: { "shortUrl": "http://short.com/xYswlE" }
     *
     * @param url
     * @param request
     * @return a JSON object, key:value map.
     */
    @PostMapping(value = "/register", produces = "application/json", consumes = "application/json")
    public Map<String, String> register(@RequestBody Url url, HttpServletRequest request) {
        Map<String, String> shortUrl = new HashMap<>();
        Url savedUrl = urlService.save(url);
        String requestUrl = request.getRequestURL().toString();
        String baseUrl = requestUrl.substring(0, requestUrl.lastIndexOf("/") + 1);
        shortUrl.put("shortUrl", String.format("%s%s", baseUrl, savedUrl.getHash()));
        return shortUrl;
    }

    /**
     * Redirect to original url if exist or 404 if not.
     * Request:
     * http GET 'http://localhost:8080/4d789a4b'
     *     'Accept:text/html'
     *
     * Response:
     * HTTP/1.1 302 Found
     * Content-Language: en
     * Location: https://habr.com/company/jugru/
     *
     * @param hash
     * @return RedirectView with original URL if exist or 404 if not.
     */
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
