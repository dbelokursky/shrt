package ru.dbelokursky.shrt.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.dbelokursky.shrt.domain.User;
import ru.dbelokursky.shrt.repository.UserRepository;

public class UserServiceImpl {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
