package com.example.exercise.controller;

import com.example.exercise.dto.UserResponse;
import com.example.exercise.exception.EmailAlreadyRegisteredException;
import com.example.exercise.service.UserService;
import com.example.exercise.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @TestConfiguration
    static class SecurityTestConfig {
        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }

    @Test
    void testRegisterUserSuccess() throws Exception {
        String requestBody = """
            {
                "name": "Juan Rodriguez",
                "email": "juan@rodriguez1.org",
                "password": "Yamaha41",
                "phones": [
                    {
                        "number": "1234567",
                        "citycode": "1",
                        "countrycode": "43"
                    }
                ]
            }
        """;

        UserResponse userResponse = new UserResponse();
        userResponse.setId("uuid-test");
        userResponse.setName("Juan Rodriguez");
        userResponse.setEmail("juan@rodriguez.org");
        userResponse.setToken("token123");
        userResponse.setIsActive(true);
        userResponse.setCreated(LocalDateTime.now());
        userResponse.setModified(LocalDateTime.now());
        userResponse.setLast_login(LocalDateTime.now());

        when(userService.registerUser(any())).thenReturn(userResponse);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("juan@rodriguez.org"))
                .andExpect(jsonPath("$.token").value("token123"));
    }

    @Test
    void testRegisterUserValidationErrors() throws Exception {
        String requestBody = """
            {
                "name": "",
                "email": "invalid-email",
                "password": "pass",
                "phones": []
            }
        """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Error de validación"))
                .andExpect(jsonPath("$.detalles.name").value("El nombre es obligatorio"))
                .andExpect(jsonPath("$.detalles.email").value("Formato de correo inválido"))
                .andExpect(jsonPath("$.detalles.phones").value("La lista de teléfonos no puede ser vacía"));
    }

    @Test
    void testRegisterUserEmailExists() throws Exception {
        String requestBody = """
            {
                "name": "Juan Rodriguez",
                "email": "juan@rodriguez.org",
                "password": "Yamaha41",
                "phones": [
                    {"number": "1234567", "citycode": "1", "countrycode": "57"}
                ]
            }
        """;

        when(userService.registerUser(any())).thenThrow(new EmailAlreadyRegisteredException("El correo ya está registrado"));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El correo ya está registrado"));
    }

}

