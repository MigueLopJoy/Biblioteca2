package com.miguel.biblioteca.mapper;

import com.miguel.biblioteca.DTO.RoleDTO;
import com.miguel.biblioteca.model.Role;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class RoleMapper {

    private final ModelMapper modelMapper;

    public Role mapDtoToEntity(RoleDTO roleDTO) {
        return modelMapper.map(roleDTO, Role.class);
    }

    public RoleDTO mapEntityToDto(Role role) {
        return modelMapper.map(role, RoleDTO.class);
    }

    public Set<RoleDTO> mapEntitySetToDtoSet(Set<Role> authorities) {
        return authorities.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toSet());
    }

    public Set<Role> mapDtoSetToEntitySet(Set<RoleDTO> authoritiesDTO) {
        return authoritiesDTO.stream()
                .map(this::mapDtoToEntity)
                .collect(Collectors.toSet());
    }
}