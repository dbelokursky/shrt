package ru.dbelokursky.shrt.repository;

import org.springframework.data.repository.CrudRepository;
import ru.dbelokursky.shrt.domain.Url;
import ru.dbelokursky.shrt.domain.User;

import java.util.Optional;
import java.util.Set;

public interface UrlRepository extends CrudRepository<Url, Long> {

    Optional<Url> findByHash(String shortUrl);

    Optional<Url> findByHashAndUser(String hash, User user);

    Set<Url> findByUserId(Long userId);
}
