package ru.dbelokursky.shrt.service;

import com.google.common.collect.Lists;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import ru.dbelokursky.shrt.domain.Account;
import ru.dbelokursky.shrt.domain.User;
import ru.dbelokursky.shrt.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final static int DEFAULT_PASSWORD_LENGTH = 8;

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login).get();
    }

    @Override
    public List<User> findAll() {
        return Lists.newArrayList(userRepository.findAll());
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Account save(User user) {
        Account account = new Account();
        if (!userRepository.findByLogin(user.getLogin()).isPresent()) {
            String password = RandomStringUtils.randomAlphanumeric(DEFAULT_PASSWORD_LENGTH);
            String salt = BCrypt.gensalt();
            String hashedPassword = BCrypt.hashpw(password, salt);
            user.setPassword(hashedPassword);
            userRepository.save(user);
            account.setSuccess(true);
            account.setDescription("Your account is opened.");
            account.setPassword(password);
        } else {
            account.setSuccess(false);
            account.setDescription("An account with that login already exists");
        }
        return account;
    }
}
