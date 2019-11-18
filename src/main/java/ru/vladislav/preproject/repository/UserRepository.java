package ru.vladislav.preproject.repository;

import org.springframework.data.repository.CrudRepository;
import ru.vladislav.preproject.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findAll();

    User save(User user);

    void deleteById(Integer id);

    Optional<User> findById(Integer id);
}