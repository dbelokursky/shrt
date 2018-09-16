package ru.dbelokursky.shrt.service;

import ru.dbelokursky.shrt.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    User findById(Long id);

    void save(User user);
}
