package com.miguel.biblioteca.services;

import java.io.Reader;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface IUReaderService {
    public Reader findByReaderCode(String readerCode);    
    public List<Reader> findByReaderName(String readerName);
    public List<Reader> getAllReaders();
    public void saveNewReader(Reader reader);
}
