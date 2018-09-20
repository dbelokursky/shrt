package ru.dbelokursky.shrt.service;

import ru.dbelokursky.shrt.domain.Account;
import ru.dbelokursky.shrt.domain.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(Long id);

    Account save(User user);

    Optional<User> findByLogin(String login);

    Map<String, Integer> getClickStatistic(String login);
}
