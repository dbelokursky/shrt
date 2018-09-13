package ru.dbelokursky.shrt.repository;

import org.springframework.data.repository.CrudRepository;
import ru.dbelokursky.shrt.domain.Url;

public interface UrlRepository extends CrudRepository<Url, Long> {
}
