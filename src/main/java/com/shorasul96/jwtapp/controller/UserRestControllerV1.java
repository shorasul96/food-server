package com.shorasul96.jwtapp.controller;

import com.shorasul96.jwtapp.dto.AuthenticationDto;
import com.shorasul96.jwtapp.dto.UserDto;
import com.shorasul96.jwtapp.model.User;
import com.shorasul96.jwtapp.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller user connected requestst.
 *
 * @author Eugene Suleimanov
 * @version 1.0
 */

@RestController
@RequestMapping(value = "/api/v1/user")
@PropertySource("classpath:application-dev.properties")
public class UserRestControllerV1 {
    private final UserService userService;

    @Autowired
    public UserRestControllerV1(UserService userService) {
        this.userService = userService;
    }

    @Value("${user.register.errorEmptyFields}")
    private String emptyField;

    @Value("${user.register.errorUsername}")
    private String errorUsername;

    @Value("${user.register.errorPassword}")
    private String errorPassword;

    @Value("${user.register.success}")
    private String successRegister;


    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") Long id) {
        User user = userService.findById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        UserDto result = UserDto.fromUser(user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserDto> login(@RequestBody AuthenticationDto authenticationDto) {

        User user = userService.findByUsername(authenticationDto.getUsername());

        if (user == null) {
            UserDto emptyUser = new UserDto();
            System.out.println("--------------------------------------test------------------------------------");
            emptyUser.setError("User not found");
            return new ResponseEntity<UserDto>(emptyUser, HttpStatus.BAD_REQUEST);
        }
        UserDto result = UserDto.fromUser(user);
        return new ResponseEntity<UserDto>(result, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerNewUser(@RequestBody UserDto userDto) {

        if (userDto.getUsername().equals("")) {
            return new ResponseEntity<>(emptyField, HttpStatus.BAD_REQUEST);
        } else if (userService.findByUsername(userDto.getUsername()) != null) {
            return new ResponseEntity<>(errorUsername, HttpStatus.BAD_REQUEST);
        } else if (userDto.getPassword().equals("")) {
            return new ResponseEntity<>(errorPassword, HttpStatus.BAD_REQUEST);
        }
        User user = userDto.toUser();
        userService.register(user);
        return new ResponseEntity<>(successRegister, HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<String> getAll() {

        String result = "Hello from server";

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
