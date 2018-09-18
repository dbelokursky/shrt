package ru.dbelokursky.shrt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.dbelokursky.shrt.domain.Account;
import ru.dbelokursky.shrt.domain.User;
import ru.dbelokursky.shrt.service.UserService;

@RestController
public class AccountController {

    private final UserService userService;

    @Autowired
    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/account", produces = "application/json", consumes = "application/json")
    public Account createAccount(@RequestBody User user) {
        return userService.save(user);
    }
}
