package application.service.impl;

import application.dto.shopping.cart.ShoppingCartResponseDto;
import application.dto.user.UserLoginRequestDto;
import application.dto.user.UserRegistrationRequestDto;
import application.dto.user.UserResponseDto;
import application.exception.RegistrationException;
import application.mapper.UserMapper;
import application.model.User;
import application.repository.UserRepository;
import application.service.ShoppingCartService;
import jakarta.inject.Named;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static final Integer ONE_TIME = 1;
    private static final String EXCEPTION = "You are already registered!";
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ShoppingCartService shoppingCartService;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    @DisplayName("""
            Verify register() method with nonRegisteredUser
            """)
    void register_NonRegisteredUser_ReturnUserResponseDto() {
        UserRegistrationRequestDto clarkRequestDto
                = new UserRegistrationRequestDto()
                .setEmail("clark@gmail.com")
                .setPassword("clark123")
                .setFirstName("clark")
                .setLastName("johnson")
                .setRepeatPassword("clark123")
                .setShippingAddress("Shevchenko 1");
        User clark = new User()
                .setId(2L)
                .setEmail(clarkRequestDto.getEmail())
                .setFirstName(clarkRequestDto.getFirstName())
                .setLastName(clarkRequestDto.getLastName())
                .setShippingAddress(clarkRequestDto.getShippingAddress());
        UserResponseDto clarkResponseDto
                = new UserResponseDto()
                .setId(clark.getId())
                .setEmail(clark.getEmail())
                .setFirstName(clark.getFirstName())
                .setLastName(clark.getLastName())
                .setShippingAddress(clark.getShippingAddress());

        when(userRepository.findUserByEmail(clarkRequestDto.getEmail()))
                .thenReturn(Optional.empty());
        when(userMapper.toModel(clarkRequestDto)).thenReturn(clark);
        when(passwordEncoder.encode(clarkRequestDto.getPassword()))
                .thenReturn("$2a$10$nA2TJNchONFwvYeKf7kALuwncLTIoFuywvK4YMa21IrRL.pcB/645");
        clark.setPassword("$2a$10$nA2TJNchONFwvYeKf7kALuwncLTIoFuywvK4YMa21IrRL.pcB/645");
        when(userRepository.save(clark)).thenReturn(clark);
        when(shoppingCartService.createShoppingCart(clark))
                .thenReturn(new ShoppingCartResponseDto().setId(2L).setUserId(clark.getId()));
        when(userMapper.toDto(clark)).thenReturn(clarkResponseDto);

        UserResponseDto actual
                = userServiceImpl.register(clarkRequestDto);
        assertNotNull(actual);
        assertEquals(clarkResponseDto, actual);
        verify(userRepository, times(ONE_TIME)).findUserByEmail(clarkRequestDto.getEmail());
        verify(userRepository, times(ONE_TIME)).save(clark);
    }

    @Test
    @DisplayName("""
            Verify register() method with registeredUser
            """)
    void register_RegisteredUser_ThrowException() {
        UserRegistrationRequestDto aliceRequestDto
                = new UserRegistrationRequestDto()
                .setEmail("alice@gmail.com")
                .setPassword("alice123")
                .setRepeatPassword("alice123")
                .setFirstName("alice")
                .setLastName("noy")
                .setShippingAddress("Franko 12");

        when(userRepository.findUserByEmail(aliceRequestDto.getEmail()))
                .thenReturn(Optional.ofNullable(
                        new User().setId(2L).setEmail("alice@gmail.com")));

        Exception exception
                = assertThrows(RegistrationException.class,
                () -> userServiceImpl.register(aliceRequestDto));
        String actual = exception.getMessage();
        assertNotNull(actual);
        assertEquals(EXCEPTION, actual);
    }
}
