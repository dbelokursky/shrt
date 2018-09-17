package ru.dbelokursky.shrt.repository;

import org.springframework.data.repository.CrudRepository;
import ru.dbelokursky.shrt.domain.Url;

import java.util.Optional;

public interface UrlRepository extends CrudRepository<Url, Long> {

    Optional<Url> findByHash(String shortUrl);
}
