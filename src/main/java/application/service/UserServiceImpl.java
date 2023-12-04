package application.service;

import application.dto.user.UserRegistrationRequestDto;
import application.dto.user.UserResponseDto;
import application.exception.RegistrationException;
import application.mapper.UserMapper;
import application.model.ShoppingCart;
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
    private final ShoppingCartService shoppingCartService;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto inputUser) {
        if (!userRepository.findUserByEmail(inputUser.getEmail()).isEmpty()) {
            throw new RegistrationException("You are already registered");
        }
      User user = User.builder()
                .email(inputUser.getEmail())
                .password(passwordEncoder.encode(inputUser.getPassword()))
                .firstName(inputUser.getFirstName())
                .lastName(inputUser.getLastName())
                .shippingAddress(inputUser.getShippingAddress())
                .build();
        User savedUser = userRepository.save(user);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(savedUser);
        shoppingCartService.save(shoppingCart);
        return userMapper.toDto(savedUser);
    }
}
