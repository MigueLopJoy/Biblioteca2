package com.miguel.biblioteca.repositories;

import com.miguel.biblioteca.model.UReader;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUReaderRepository extends JpaRepository<UReader, Integer> {
    public Optional<UReader> findByReaderCode(String readerCode);

    @Query("SELECT r FROM UReader r WHERE CONCAT(r.firstName, ' ', r.lastName) = :userName")
    public List<UReader> findByFullName(@Param("userName") String userName);
}
