package com.example.exercise.service;

import com.example.exercise.dto.PhoneResponse;
import com.example.exercise.dto.UserRequest;
import com.example.exercise.dto.UserResponse;
import com.example.exercise.exception.EmailAlreadyRegisteredException;
import com.example.exercise.exception.InvalidDataException;
import com.example.exercise.model.Phone;
import com.example.exercise.model.User;
import com.example.exercise.repository.UserRepository;
import com.example.exercise.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Pattern emailPattern;
    private final Pattern passwordPattern;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           Pattern emailPattern,
                           Pattern passwordPattern,
                           JwtUtil jwtUtil,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailPattern = emailPattern;
        this.passwordPattern = passwordPattern;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse registerUser(UserRequest userRequest) {
        if (userRequest.getName() == null || userRequest.getName().isBlank()) {
            throw new InvalidDataException("El nombre es obligatorio");
        }
        if (userRequest.getEmail() == null || userRequest.getEmail().isBlank()) {
            throw new InvalidDataException("El email es obligatorio");
        }
        if (userRequest.getPassword() == null || userRequest.getPassword().isBlank()) {
            throw new InvalidDataException("La contraseña es obligatoria");
        }

        if (userRequest.getPhones() == null || userRequest.getPhones().isEmpty()) {
            throw new InvalidDataException("Se debe proporcionar al menos un teléfono");
        }

        if (!emailPattern.matcher(userRequest.getEmail()).matches()) {
            throw new InvalidDataException("Formato de email inválido");
        }

        if (!passwordPattern.matcher(userRequest.getPassword()).matches()) {
            throw new InvalidDataException("Formato de contraseña inválido");
        }

        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyRegisteredException("El correo ya está registrado");
        }

        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        String hashedPassword = passwordEncoder.encode(userRequest.getPassword());
        user.setPassword(hashedPassword);

        List<Phone> phones = userRequest.getPhones().stream().map(phoneRequest -> {
            Phone phone = new Phone();
            phone.setNumber(phoneRequest.getNumber());
            phone.setCitycode(phoneRequest.getCitycode());
            phone.setCountrycode(phoneRequest.getCountrycode());
            return phone;
        }).collect(Collectors.toList());
        user.setPhones(phones);

        LocalDateTime now = LocalDateTime.now();
        user.setCreated(now);
        user.setModified(now);
        user.setLastLogin(now);
        user.setIsActive(true);
        String token = jwtUtil.generateToken(user.getEmail());
        user.setToken(token);

        User savedUser = userRepository.save(user);

        List<PhoneResponse> phoneResponses = savedUser.getPhones().stream()
                .map(phone -> new PhoneResponse(phone.getNumber(), phone.getCitycode(), phone.getCountrycode()))
                .collect(Collectors.toList());

        return getUserResponse(savedUser, phoneResponses);
    }

    private static UserResponse getUserResponse(User savedUser, List<PhoneResponse> phoneResponses) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setCreated(savedUser.getCreated());
        userResponse.setModified(savedUser.getModified());
        userResponse.setLast_login(savedUser.getLastLogin());
        userResponse.setToken(savedUser.getToken());
        userResponse.setIsActive(savedUser.getIsActive());
        userResponse.setName(savedUser.getName());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setPhones(phoneResponses);
        return userResponse;
    }
}
