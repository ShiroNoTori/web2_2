package ru.vladislav.preproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.vladislav.preproject.model.User;
import ru.vladislav.preproject.repository.UserRepository;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserRepository repository;

    @Autowired
    UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String index(Model model, Principal principal) {
        model.addAttribute("name", principal.getName());
        return "userIndex";
    }
}