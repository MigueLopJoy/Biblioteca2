
package com.miguel.biblioteca.mapper;

import com.miguel.biblioteca.DTO.UserDTO;
import com.miguel.biblioteca.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUserCode(userDTO.getUserCode());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        // Set other attributes as needed
        return user;
    }
}
