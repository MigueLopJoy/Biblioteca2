package com.miguel.biblioteca.services;

import java.io.Reader;
import java.util.List;

import com.miguel.biblioteca.model.UReader;

public interface IUReaderService {
    public UReader findByReaderCode(String readerCode);
    public List<UReader> findByReaderName(String readerName);
    public List<UReader> getAllReaders();
    public UReader saveNewReader(Reader reader);
}
