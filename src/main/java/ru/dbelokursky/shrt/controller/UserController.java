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

    /**
     * Request:
     * JSON object with the following parameters:
     * AccountId (String, mandatory)
     * Example:
     * {
     * "login": "userLogin"
     * }
     * <p>
     * Response:
     * Unsuccessful registration occurs only if the concerned login
     * already exists. The parameters are as follows:
     * success: true | false
     * description: Description of status, for example: account with that
     * login already exists
     * password: Returns only if the account was successfully created.
     * Automatically generated password length of 8 alphanumeric
     * characters
     * Example:
     * {
     * "success": true,
     * "description": "Your account is opened.",
     * "password": "My6KoIV9"
     * }
     *
     * @param user
     * @return
     */
    @PostMapping(value = "/account", produces = "application/json", consumes = "application/json")
    public Account createAccount(@RequestBody User user) {
        return userService.save(user);
    }

    /**
     * The server responds with a JSON object, key:value map, where the key
     * is the registered URL, and the value is the number of this URL redirects.
     * Example:
     * {
     * "http://myweb.com/someverylongurl/thensomedirectory/": "10",
     * "http://myweb.com/someverylongurl2/thensomedirectory2/": "4",
     * "http://myweb.com/someverylongurl3/thensomedirectory3/": "91",
     * }
     * @param login
     * @return
     */
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
