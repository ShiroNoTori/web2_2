package ru.vladislav.preproject.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.vladislav.preproject.model.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

    Role save(Role role);

    Role findByName(String name);
}
