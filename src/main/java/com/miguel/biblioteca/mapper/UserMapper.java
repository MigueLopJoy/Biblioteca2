
package com.miguel.biblioteca.mapper;

import com.miguel.biblioteca.DTO.UserDTO;
import com.miguel.biblioteca.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    
    private static final ModelMapper modelMapper = new ModelMapper();

    public static User mapDtoToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public static UserDTO mapEntityToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
