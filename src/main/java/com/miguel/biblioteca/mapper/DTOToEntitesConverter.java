package com.miguel.biblioteca.mapper;

import com.miguel.biblioteca.model.User;
import com.miguel.biblioteca.DTOS.UserDTO;

@Component
public class DTOToEntitesConverter {
    
    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUserCode(userDTO.getUserCode());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        // Set other attributes as needed
        return user;
    }
}
