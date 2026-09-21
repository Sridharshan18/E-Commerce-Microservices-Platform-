package com.ecommerce.User.controllers;



import com.ecommerce.User.DTO.UserRequest;
import com.ecommerce.User.DTO.UserResponse;
import com.ecommerce.User.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/api/users")
    public ResponseEntity<List<UserResponse>> getAllUsers()
    {
        return new ResponseEntity<>(userService.fetchAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id)
    {
        Optional<UserResponse> user = userService.fetchUserById(id);

        return user.map(userResponse -> new ResponseEntity<>(userResponse, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping("/api/users")
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest)
    {
        userService.addUser(userRequest);
        return new ResponseEntity<>("Added User Successfully",HttpStatus.CREATED);
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id , @RequestBody UserRequest updatedUserRequest)
    {
        boolean updated =  userService.updateUser(id , updatedUserRequest);

        if(updated)
        {
            return new ResponseEntity<>("Updated User Successfully",HttpStatus.OK);
        }

        return new ResponseEntity<>(" User Not Found",HttpStatus.NOT_FOUND);
    }
}
