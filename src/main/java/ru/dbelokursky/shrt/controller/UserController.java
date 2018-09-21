package ru.dbelokursky.shrt.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.dbelokursky.shrt.domain.Account;
import ru.dbelokursky.shrt.domain.User;
import ru.dbelokursky.shrt.service.UrlService;
import ru.dbelokursky.shrt.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;

    private final UrlService urlService;

    public UserController(UserService userService, UrlService urlService) {
        this.userService = userService;
        this.urlService = urlService;
    }

    @PostMapping(value = "/account", produces = "application/json", consumes = "application/json")
    public Account createAccount(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping(value = "/statistic/{login}")
    public Map<String, Integer> getStatistic(@PathVariable String login) {
        Map<String, Integer> stat = new HashMap<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getName().equals(login)) {
            stat = userService.getClickStatistic(login);
        }
        return stat;
    }
}
