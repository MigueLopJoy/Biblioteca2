package com.miguel.library.repository;

import com.miguel.library.model.UReader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUReaderRepository extends JpaRepository<UReader, Integer> {

    public Optional<UReader> findByReaderNumber(String readerNumber);

}
