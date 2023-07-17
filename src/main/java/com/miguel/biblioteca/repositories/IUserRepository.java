package com.miguel.biblioteca.repositories;

import com.miguel.biblioteca.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IUserRepository extends JpaRepository<User, Integer> {}
