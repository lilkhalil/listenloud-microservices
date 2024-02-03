package com.lilkhalil.user.controller;

import com.lilkhalil.user.domain.User;
import com.lilkhalil.user.dto.RegistrationRequest;
import com.lilkhalil.user.dto.UpdateRequest;
import com.lilkhalil.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody RegistrationRequest request)
    {
        return userService.createUser(request);
    }

    @GetMapping("/{id}")
    public User readUser(@PathVariable("id") Long id)
    {
        return userService.readUserById(id);
    }

    @GetMapping
    public List<User> readUsers()
    {
        return userService.readAllUsers();
    }

    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable("id") Long id,
            @RequestBody UpdateRequest request)
    {
        return userService.updateUser(id, request);
    }

    @PostMapping("/{id}")
    public User uploadProfileImage(
            @PathVariable("id") Long id,
            @RequestParam("file") MultipartFile file
    )
    {
        return userService.uploadProfileImage(id, file);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable("id") Long id)
    {
        userService.deleteUserById(id);
    }

}
