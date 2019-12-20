package ru.vladislav.preproject.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.vladislav.preproject.exception.UserNotFoundException;
import ru.vladislav.preproject.model.Role;
import ru.vladislav.preproject.model.User;
import ru.vladislav.preproject.repository.RoleRepository;
import ru.vladislav.preproject.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/v1")
public class RestApiController {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder encoder;

    @Autowired
    public RestApiController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    /*@PostMapping("/user/add")
    @ResponseBody
    public ResponseEntity<User> addUser(@RequestParam(name = "login") String login,
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
        return new ResponseEntity<>(user, HttpStatus.OK);
    }*/

    @RequestMapping(value = "/user/add" , method = RequestMethod.POST)
    public @ResponseBody User addUser(@RequestBody User user,
                                      @RequestParam(name = "roles") String roleName) {
        user = setRoles(user, roleName);

        userRepository.save(user);
        return user;
    }

    @GetMapping("/user/{id}")
    @ResponseBody
    public ResponseEntity<User> get(@PathVariable Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/user/all")
    @ResponseBody
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @PostMapping("/update")
    public @ResponseBody User update(@RequestBody User user,
                                     @RequestParam(name = "roles") String roleName) {
        int id = user.getId();
        userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        
        user = setRoles(user, roleName);

        userRepository.save(user);
        return user;
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<User> delete(@PathVariable Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        user.getRoles().clear();
        userRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private User setRoles(User user, String roleName) {
        List<Role> roleList = new ArrayList<Role>();
        if (roleName.contains("ADMIN")) {
            roleList.add(roleRepository.findByName("ROLE_ADMIN"));
        }
        roleList.add(roleRepository.findByName("ROLE_USER"));
        user.setRoles(roleList);
        return user;
    }
}