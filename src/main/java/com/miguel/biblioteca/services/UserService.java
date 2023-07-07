package com.miguel.biblioteca.services;

import com.miguel.biblioteca.model.User;
import com.miguel.biblioteca.repositories.IUserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{

    @Autowired
    private IUserRepository userRepository;
    
    @Override
    public User findByUserCode(String userCode) {
        return userRepository.findByUserCode(userCode).orElse(null);
    }

    @Override
    public List<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void saveNewUser(User user) {
        userRepository.save(user);
    }   
}
