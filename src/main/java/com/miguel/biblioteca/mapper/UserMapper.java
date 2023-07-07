
package com.miguel.biblioteca.mapper;

import com.miguel.biblioteca.DTO.UserDTO;
import com.miguel.biblioteca.model.User;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    
    @Autowired
    private final ModelMapper modelMapper;
    
    @Autowired
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User mapDtoToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public UserDTO mapEntityToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
    
    public List<UserDTO> mapEntityListToDtoList(List<User> users) {
        return users.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }
}
