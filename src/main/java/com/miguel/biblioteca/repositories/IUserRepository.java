package com.miguel.biblioteca.repositories;

import com.miguel.biblioteca.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    public Optional<User> findByUserCode(String userCode);   
    
    @Query("SELECT u FROM User u WHERE CONCAT(u.firstName, ' ', u.lastNames) = :userName")
    public List<User> findByUserName(@Param("userName") String userName);
}
