package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Set;


public interface UserService {
    void saveUser(User user);

    void updateUser(long id, User user);

    void removeUser(long id);

    List<User> getAllUsers();

    Set<Role> getAllRoles();

    Set<Role> getByName(String name);

    User getUser(Long id);

    User getUserByUsername(String username);
}
