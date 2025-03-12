package com.example.exercise.service;

import com.example.exercise.dto.UserRequest;
import com.example.exercise.dto.UserResponse;

public interface UserService {
    UserResponse registerUser(UserRequest userRequest);
}
