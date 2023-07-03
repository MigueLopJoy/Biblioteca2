package com.miguel.biblioteca.controllers;

import com.miguel.biblioteca.DTO.UserDTO;
import com.miguel.biblioteca.model.User;
import com.miguel.biblioteca.services.IUserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private IUserService userService;
    
    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/get-user-by-name")
    public ResponseEntity<List<User>> getUserByUserName(UserDTO userDTO) {
        List<User> users = userService.findByUserName(userDTO.getFirstName() + ' ' + userDTO.getLastName());
        return ResponseEntity.ok(users);
    }
}
