package com.miguel.library.services;

import com.miguel.library.model.User;
import com.miguel.library.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImpUserService implements IUserService {

    @Autowired
    public IUserRepository userRepository;

    @Override
    public User searchByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User searchByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElse(null);
    }
}
