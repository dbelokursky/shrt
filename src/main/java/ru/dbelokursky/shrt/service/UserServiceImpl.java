package ru.dbelokursky.shrt.service;

import com.google.common.collect.Lists;
import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import ru.dbelokursky.shrt.domain.Account;
import ru.dbelokursky.shrt.domain.Role;
import ru.dbelokursky.shrt.domain.Url;
import ru.dbelokursky.shrt.domain.User;
import ru.dbelokursky.shrt.repository.UrlRepository;
import ru.dbelokursky.shrt.repository.UserRepository;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final static int DEFAULT_PASSWORD_LENGTH = 8;

    private final UrlRepository urlRepository;

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UrlRepository urlRepository, UserRepository userRepository) {
        this.urlRepository = urlRepository;
        this.userRepository = userRepository;
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
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
        Account account = Account.builder().build();
        try {
            String password = RandomStringUtils.randomAlphanumeric(DEFAULT_PASSWORD_LENGTH);
            String salt = BCrypt.gensalt();
            String hashedPassword = BCrypt.hashpw(password, salt);
            user.setPassword(hashedPassword);
            user.setEnabled(true);
            HashSet roles = new HashSet();
            Role defaultRole = Role.builder().id(1L).name("ROLE_USER").build();
            roles.add(defaultRole);
            user.setRoles(roles);
            userRepository.save(user);
            account.setSuccess(true);
            account.setDescription("Your account is opened.");
            account.setPassword(password);
        } catch (DataIntegrityViolationException e) {
            account.setSuccess(false);
            account.setDescription("An account with that login already exists");
        }
        return account;
    }

    @Override
    public Map<String, Integer> getClickStatistic(String login) {
        Map<String, Integer> userUrls = new HashMap<>();
        Optional<User> user = findByLogin(login);
        if (user.isPresent()) {
            for (Url url : urlRepository.findByUserId(user.get().getId())) {
                userUrls.put(url.getUrl(), url.getClickCounter());
            }
        }
        return userUrls;
    }
}
