package com.miguel.library.services;

import com.miguel.library.model.UReader;
import com.miguel.library.model.User;

public interface IUserService {
    public User searchByEmail(String email);

    public User searchByPhoneNumber(String phoneNumber);
}
