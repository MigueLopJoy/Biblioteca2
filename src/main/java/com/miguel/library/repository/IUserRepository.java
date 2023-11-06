package com.miguel.library.repository;

import com.miguel.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {

    public Optional<User> findByEmail(String email);

    public Optional<User> findByPhoneNumber(String phoneNumber);


}
