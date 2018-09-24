package ru.dbelokursky.shrt.service;

import ru.dbelokursky.shrt.domain.Url;
import ru.dbelokursky.shrt.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UrlService {

    List<Url> findAll();

    Optional<Url> findById(Long id);

    Optional<Url> findByHashAndLogin(String hash, User user);

    Url save(Url url);

    Url incrementCounter(Url url);

    Optional<Url> findByHash(String hash);

    Set<Url> findByUserId(Long userId);
}
