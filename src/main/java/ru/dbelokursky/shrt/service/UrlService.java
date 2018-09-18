package ru.dbelokursky.shrt.service;

import ru.dbelokursky.shrt.domain.Url;

import java.util.List;
import java.util.Optional;

public interface UrlService {

    List<Url> findAll();

    Optional<Url> findById(Long id);

    Url save(Url url);

    Url incrementCounter(Url url);

    Optional<Url> findByHash(String hash);
}
