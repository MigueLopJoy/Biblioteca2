package com.miguel.biblioteca.services;

import com.miguel.biblioteca.model.User;
import java.util.List;

public interface IUserService {
    public User findByUserCode(String userCode);    
    public List<User> findByUserName(String userName);
    public List<User> getAllUsers();
    public void saveNewUser(User user);
}
