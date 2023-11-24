package application.service;

import application.dto.user.UserRegistrationRequestDto;
import application.dto.user.UserResponseDto;
import application.exception.RegistrationException;
import application.mapper.UserMapper;
import application.model.User;
import application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RegistrationException("You are already registered");
        }
        User savedUser = userRepository.save(userMapper.toModel(user));
        return userMapper.toDto(savedUser);
    }
}
