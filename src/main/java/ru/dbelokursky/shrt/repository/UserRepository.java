package ru.dbelokursky.shrt.repository;

import org.springframework.data.repository.CrudRepository;
import ru.dbelokursky.shrt.domain.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByLogin(String login);
}
