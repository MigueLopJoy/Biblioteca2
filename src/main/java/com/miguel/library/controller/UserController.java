package com.miguel.library.controller;

import com.miguel.library.DTO.UserDTOChangePasswordRequest;
import com.miguel.library.DTO.UserDTOUserResponse;
import com.miguel.library.services.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin()
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private LogoutHandler logoutHandler;

    @PostMapping("/change-password")
    public ResponseEntity<UserDTOUserResponse> changePassword(
            @Valid @RequestBody UserDTOChangePasswordRequest passwordRequest,
            Principal connectedUser
    ) {
        return ResponseEntity.ok(userService.changeUserPassword(passwordRequest, connectedUser));
    }

    @GetMapping("/me")
    public UserDetails getCurrentUser() {
        // Obtener la autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verificar si el usuario está autenticado
        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            // Obtener los detalles del usuario desde la autenticación
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails;
        } else {
            // Manejar el caso en que el usuario no esté autenticado
            // Puedes devolver null o algún otro valor según tus requisitos
            return null;
        }
    }
    @PostMapping("/get-connected-user")
    public ResponseEntity<?> getConnectedUser(
            Principal connectedUser
    ) {
        return ResponseEntity.ok(userService.getConnectedUser(connectedUser));
    }
}
