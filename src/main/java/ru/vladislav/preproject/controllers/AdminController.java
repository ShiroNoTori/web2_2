package ru.vladislav.preproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.vladislav.preproject.model.Role;
import ru.vladislav.preproject.model.User;
import ru.vladislav.preproject.repository.RoleRepository;
import ru.vladislav.preproject.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder encoder;

    @Autowired
    AdminController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @PostMapping("/add")
    public String addUser(@RequestParam(name = "login") String login,
                          @RequestParam(name = "name") String name,
                          @RequestParam(name = "password") String password,
                          @RequestParam(name = "role") String roleName) {
        List<Role> roles = new ArrayList<Role>();
        roles.add(roleRepository.findByName(roleName));
        if (roleName.contains("ADMIN")) {
            roles.add(roleRepository.findByName("ROLE_USER"));
        }

        User user = new User(login, name, encoder.encode(password), roles);
        userRepository.save(user);
        return "redirect:/admin/all";
    }

    @GetMapping("/add")
    public String addUser() {
        return "userAdd";
    }

    @GetMapping("/all")
    public String getAll(Model model) {
        model.addAttribute("userList", userRepository.findAll());
        return "userList";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        userRepository.findById(id).get().getRoles().clear();
        userRepository.deleteById(id);
        return "redirect:/admin/all";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Integer id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "userUpdate";
    }

    @PostMapping("/update")
    public String update(@RequestParam(name = "id") Integer id,
                         @RequestParam(name = "name") String name,
                         @RequestParam(name = "password") String password) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        user.setName(name);
        if (!password.isEmpty()) {
            user.setPassword(encoder.encode(password));
        }

        userRepository.save(user);
        return "redirect:/admin/all";
    }

}
