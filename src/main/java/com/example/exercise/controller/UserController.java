package com.example.exercise.controller;

import com.example.exercise.dto.UserRequest;
import com.example.exercise.dto.UserResponse;
import com.example.exercise.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint para registrar un nuevo usuario.
     *
     * Recibe un JSON con los campos:
     * {
     *   "name": "Juan Rodriguez",
     *   "email": "juan@rodriguez.org",
     *   "password": "hunter2",
     *   "phones": [
     *       {
     *           "number": "1234567",
     *           "citycode": "1",
     *           "contrycode": "57"
     *       }
     *   ]
     * }
     *
     * Retorna un JSON con los datos del usuario registrado, incluyendo:
     * - id (UUID)
     * - created (fecha de creación)
     * - modified (fecha de última modificación)
     * - last_login (fecha del último ingreso, para nuevo usuario coincide con created)
     * - token (token de acceso, puede ser JWT)
     * - isActive (indica si el usuario está activo)
     *
     * @param userRequest objeto con la información del usuario a registrar.
     * @return ResponseEntity con el usuario registrado y código HTTP 201.
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.registerUser(userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }
}
