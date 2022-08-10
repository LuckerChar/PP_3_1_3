package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @GetMapping()
    public String showAll(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", userService.findByEmail(username));
        model.addAttribute("roles", userService.getSetOfRoles());
        model.addAttribute("newUser", new User());
        return "admin";
    }

    @GetMapping("/{id}")
    public String info(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("users", userService.getUser(id));
        return "admin/show";
    }

    @GetMapping("/new")
    public String addNewUser(ModelMap model, User user) {
        model.addAttribute("users", new User());
        model.addAttribute("roles", roleRepository.findAll());
        return "admin/new";
    }

    @PostMapping("/new")
    private String createUser(@ModelAttribute("users") User user,
                              @RequestParam("roles") List<String> role) {
        List<Role> roles = userService.getSetOfRoles(role);
        user.setRoles(roles);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return "redirect:/admin";

    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("user", userService.getUser(id));
        return "admin/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("user") User user,
                         @PathVariable("id") long id,
                         @RequestParam("roleList") List<String> role) {
//        if (user.getEmail().equals(userService.getUser(user.getId()).getEmail())) {
//            if (bindingResult.hasErrors())
//                return "/{id}/edit";
//        }
        System.out.println("BEGIN");
        if (!user.getPassword().equals(userService.getUser(user.getId()).getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        user.setRoles(userService.getSetOfRoles(role));
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.removeUser(id);
        return "redirect:/admin";

    }

}

