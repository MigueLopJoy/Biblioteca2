package com.miguel.biblioteca.repositories;

import com.miguel.biblioteca.model.UReader;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUReaderRepository extends IUserRepository{
    public Optional<UReader> findByReaderCode(String readerCode);   
    
    @Query("SELECT u FROM User u WHERE CONCAT(u.firstName, ' ', u.lastName) = :userName")
    public List<UReader> findByReaderName(@Param("userName") String userName);
}
