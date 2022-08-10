package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    @Override
    public void saveUser(User user) {
        if (userRepository.findByUsername(user.getEmail()) != null) {
            throw new RuntimeException();
        }
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void updateUser(long id, User user) {
        if (userRepository.findByUsername(user.getEmail()) != null) {
            throw new RuntimeException();
        }
        userRepository.saveAndFlush(user);
    }

    @Transactional
    @Override
    public void removeUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Set<Role> getAllRoles() {
        Set<Role> roles = new HashSet<>(roleRepository.findAll());
        return roles;
    }

    @Override
    public Set<Role> getByName(String name) {
        TypedQuery<Role> query = entityManager.createQuery("select role from Role role where role.name = :name", Role.class);
        query.setParameter("name", name);
        return query.getResultStream().collect(Collectors.toSet());
    }

    @Override
    public User getUser(Long id) {
        return userRepository.getById((long) Math.toIntExact(id));
    }

    @Override
    public User getUserByUsername(String email) {
        return userRepository.findByUsername(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(email);
        if (user == null) {
            throw new UsernameNotFoundException("USER NF");
        }
        return user;
    }
}
