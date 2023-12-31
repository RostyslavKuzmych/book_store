package application.service.impl;

import application.dto.user.UserRegistrationRequestDto;
import application.dto.user.UserResponseDto;
import application.exception.RegistrationException;
import application.mapper.UserMapper;
import application.model.Role;
import application.model.User;
import application.repository.UserRepository;
import application.service.ShoppingCartService;
import application.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String EXCEPTION = "You are already registered!";
    private static final Long USER_ROLE_ID = 1L;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ShoppingCartService shoppingCartService;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (!userRepository.findUserByEmail(requestDto.getEmail()).isEmpty()) {
            throw new RegistrationException(EXCEPTION);
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRoleSet(Set.of(new Role().setId(USER_ROLE_ID)));
        User savedUser = userRepository.save(user);
        shoppingCartService.createShoppingCart(savedUser);
        return userMapper.toDto(savedUser);
    }
}
