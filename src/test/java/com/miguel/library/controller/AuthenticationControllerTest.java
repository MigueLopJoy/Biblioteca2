package com.miguel.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miguel.library.DTO.*;
import com.miguel.library.model.Library;
import com.miguel.library.model.Role;
import com.miguel.library.model.ULibrarian;
import com.miguel.library.services.ImpAuthenticationService;
import org.apache.coyote.Response;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class AuthenticationControllerTest {


    @Mock
    private ImpAuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void register() throws Exception {
        AuthRegisterRequest authRequest = createSampleRegisterRequest();
        AuthRegisterResponse authResponse
                = new AuthRegisterResponse(
                        "New Library Account Created Successfully",
                        new Library(),
                        new ULibrarian());

        when(authenticationService.register(authRequest)).thenReturn(authResponse);

        ResponseEntity<AuthRegisterResponse> currentResponse = authenticationController.register(authRequest);

        assertEquals(200, currentResponse.getStatusCode());
        assertEquals(currentResponse.getBody(), authResponse);
    }

    private AuthRegisterRequest createSampleRegisterRequest() {
        AuthRegisterRequest request = new AuthRegisterRequest();

        LibraryDTOSaveLibrary libraryDTO = new LibraryDTOSaveLibrary();
        libraryDTO.setLibraryName("Example Library");
        libraryDTO.setLibraryPhoneNumber("+1234567890");
        libraryDTO.setLibraryEmail("library@example.com");
        libraryDTO.setLibraryAddress("123 Main Street");
        libraryDTO.setCity("Cityville");
        libraryDTO.setProvince("Provinceland");
        libraryDTO.setPostalCode("12345-6789");

        UserDTOSaveUser userDTO = new UserDTOSaveUser();
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setGender('M');
        userDTO.setBirthYear(1990);
        userDTO.setPhoneNumber("+9876543210");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setPassword("P@ssw0rd");

        request.setLibrary(libraryDTO);
        request.setLibrarian(userDTO);

        return request;
    }
}