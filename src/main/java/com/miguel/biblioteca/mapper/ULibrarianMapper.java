package com.miguel.biblioteca.mapper;

import com.miguel.biblioteca.DTO.RoleDTO;
import com.miguel.biblioteca.DTO.ULibrarianDTO;
import com.miguel.biblioteca.model.Author;
import com.miguel.biblioteca.model.Role;
import com.miguel.biblioteca.model.ULibrarian;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ULibrarianMapper {
    
    private final ModelMapper modelMapper;

    private final RoleMapper roleMapper;

    public ULibrarian mapDtoToEntity(ULibrarianDTO uLibrarianDTO) {
        ULibrarian uLibrarian = modelMapper.map(uLibrarianDTO, ULibrarian.class);
        Set<RoleDTO> authoritiesDTO = uLibrarianDTO.getAuthoritiesDTO();

        if (authoritiesDTO != null) {
            Set<Role> authorities = roleMapper.mapDtoSetToEntitySet(authoritiesDTO);
            uLibrarian.setAuthorities(authorities);
        }
        return uLibrarian;
    }

    public ULibrarianDTO mapEntityToDto(ULibrarian uLibrarian) {
        ULibrarianDTO uLibrarianDTO = modelMapper.map(uLibrarian, ULibrarianDTO.class);
        Set<Role> authorities = uLibrarian.getAuthorities();

        if (authorities != null) {
            Set<RoleDTO> authoritiesDTO = roleMapper.mapEntitySetToDtoSet(authorities);
            uLibrarianDTO.setAuthoritiesDTO(authoritiesDTO);
        }

        return uLibrarianDTO;
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
