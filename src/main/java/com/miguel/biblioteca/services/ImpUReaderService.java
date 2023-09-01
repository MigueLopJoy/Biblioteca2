package com.miguel.biblioteca.services;

import com.miguel.biblioteca.model.UReader;
import com.miguel.biblioteca.repositories.IUReaderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.util.List;

@AllArgsConstructor
@Service
public class ImpUReaderService implements  IUReaderService{

    private final IUReaderRepository readerRepository;
    @Override
    public UReader findByReaderCode(String readerCode) {
        return readerRepository.findByReaderCode(readerCode).orElse(null);
    }

    @Override
    public List<UReader> findByReaderName(String readerName) {
        return null;
    }

    @Override
    public List<UReader> getAllReaders() {
        return null;
    }

    @Override
    public UReader saveNewReader(Reader reader) {
        return null;
    }
}
