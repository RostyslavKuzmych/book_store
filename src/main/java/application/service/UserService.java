package application.service;

import application.dto.user.UserRegistrationRequestDto;
import application.dto.user.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto user);
}
