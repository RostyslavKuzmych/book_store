package application.service;

import application.dto.user.UserRegistrationRequestDto;
import application.dto.user.UserResponseDto;
import application.exception.RegistrationException;
import application.mapper.UserMapper;
import application.model.User;
import application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto inputUser) {
        if (userRepository.getUserByEmail(inputUser.getEmail()) != null) {
            throw new RegistrationException("You are already registered");
        }
        User user = new User();
        user.setEmail(inputUser.getEmail());
        user.setPassword(passwordEncoder.encode(inputUser.getPassword()));
        user.setFirstName(inputUser.getFirstName());
        user.setLastName(inputUser.getLastName());
        user.setShippingAddress(inputUser.getShippingAddress());
        return userMapper.toDto(userRepository.save(user));
    }
}
