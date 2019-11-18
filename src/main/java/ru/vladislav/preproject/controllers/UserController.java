package ru.vladislav.preproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.vladislav.preproject.model.User;
import ru.vladislav.preproject.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserRepository repository;

    @Autowired
    UserController(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/add")
    public String addUser(@RequestParam(name = "login") String login,
                          @RequestParam(name = "name") String name,
                          @RequestParam(name = "password") String password) {
        User user = new User(login, name, password);
        repository.save(user);
        return "redirect:/user/all";
    }

    @GetMapping("/add")
    public String addUser() {
        return "userAdd";
    }

    @GetMapping("/all")
    public String getAll(Model model) {
        model.addAttribute("userList", repository.findAll());
        return "userList";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        repository.deleteById(id);
        return "redirect:/user/all";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Integer id, Model model) {
        User user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "userUpdate";
    }

    @PostMapping("/update")
    public String update(@RequestParam(name = "id") Integer id,
                         @RequestParam(name = "name") String name,
                         @RequestParam(name = "password") String password) {
        User user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        user.setName(name);
        user.setPassword(password);

        repository.save(user);
        return "redirect:/user/all";
    }

}