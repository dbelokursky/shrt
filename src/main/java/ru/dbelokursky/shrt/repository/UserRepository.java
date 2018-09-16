package ru.dbelokursky.shrt.repository;

import org.springframework.data.repository.CrudRepository;
import ru.dbelokursky.shrt.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByLogin(String login);
}
