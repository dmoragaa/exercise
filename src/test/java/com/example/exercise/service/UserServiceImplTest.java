package com.example.exercise.service;

import com.example.exercise.dto.PhoneRequest;
import com.example.exercise.dto.UserRequest;
import com.example.exercise.dto.UserResponse;
import com.example.exercise.exception.EmailAlreadyRegisteredException;
import com.example.exercise.exception.InvalidDataException;
import com.example.exercise.model.User;
import com.example.exercise.repository.UserRepository;
import com.example.exercise.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    private final Pattern emailPattern = Pattern.compile("^[^@]+@[^@]+\\.[a-zA-Z]{2,}$");
    private final Pattern passwordPattern = Pattern.compile("^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$");

    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        userService = new UserServiceImpl(userRepository, emailPattern, passwordPattern, jwtUtil, passwordEncoder);
    }

    @Test
    void testRegisterUserSuccess() {
        PhoneRequest phoneRequest = new PhoneRequest("1234567", "1", "57");
        UserRequest userRequest = new UserRequest("Juan Rodriguez", "juan@rodriguez.org", "Yamaha41", List.of(phoneRequest));

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encryptedPassword");
        when(jwtUtil.generateToken(anyString())).thenReturn("token123");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(UUID.randomUUID().toString());
            return u;
        });

        UserResponse response = userService.registerUser(userRequest);

        assertNotNull(response.getId());
        assertEquals("Juan Rodriguez", response.getName());
        assertEquals("juan@rodriguez.org", response.getEmail());
        assertEquals("token123", response.getToken());
        assertTrue(response.getIsActive());
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        PhoneRequest phoneRequest = new PhoneRequest("1234567", "1", "57");
        UserRequest userRequest = new UserRequest("Juan Rodriguez", "juan@rodriguez.org", "Yamaha41", List.of(phoneRequest));

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        EmailAlreadyRegisteredException ex = assertThrows(EmailAlreadyRegisteredException.class, () -> {
            userService.registerUser(userRequest);
        });

        assertEquals("El correo ya está registrado", ex.getMessage());
    }

    @Test
    void testRegisterUser_InvalidEmailFormat() {
        PhoneRequest phoneRequest = new PhoneRequest("1234567", "1", "57");
        UserRequest userRequest = new UserRequest("Juan Rodriguez", "email-invalido", "Yamaha41", List.of(phoneRequest));

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> {
            userService.registerUser(userRequest);
        });

        assertEquals("Formato de email inválido", ex.getMessage());
    }

    @Test
    void testRegisterUser_InvalidPasswordFormat() {
        PhoneRequest phoneRequest = new PhoneRequest("1234567", "1", "57");
        UserRequest userRequest = new UserRequest("Juan Rodriguez", "juan@rodriguez.org", "password", List.of(phoneRequest));

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> {
            userService.registerUser(userRequest);
        });

        assertEquals("Formato de contraseña inválido", ex.getMessage());
    }

    @Test
    void testRegisterUser_PhoneListEmpty() {
        UserRequest userRequest = new UserRequest("Juan Rodriguez", "juan@rodriguez.org", "Yamaha41", List.of());

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> {
            userService.registerUser(userRequest);
        });

        assertEquals("Se debe proporcionar al menos un teléfono", ex.getMessage());
    }
}
