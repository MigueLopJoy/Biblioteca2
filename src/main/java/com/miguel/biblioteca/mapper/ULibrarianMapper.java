package com.miguel.biblioteca.mapper;

import com.miguel.biblioteca.DTO.ULibrarianDTO;
import com.miguel.biblioteca.model.ULibrarian;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ULibrarianMapper {
    
    @Autowired
    private final ModelMapper modelMapper;
    
    public ULibrarian mapDtoToEntity(ULibrarianDTO uLibrarianDTO) {
        return modelMapper.map(uLibrarianDTO, ULibrarian.class);
    }
    
    public ULibrarianDTO mapEntityToDto(ULibrarian uLibrarian) {
        return modelMapper.map(uLibrarian, ULibrarianDTO.class); 
    }
    
    public List<ULibrarianDTO> mapEntityListToDtoList(List<ULibrarian> librarians) {
        return librarians.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }
    
    public List<ULibrarian> mapDtoListToEntityList(List<ULibrarianDTO> librariansDTO) {
        return librariansDTO.stream()
                .map(this::mapDtoToEntity)
                .collect(Collectors.toList());
    }
}
