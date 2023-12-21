package application.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import application.dto.shopping.cart.ShoppingCartResponseDto;
import application.dto.user.UserRegistrationRequestDto;
import application.dto.user.UserResponseDto;
import application.exception.RegistrationException;
import application.mapper.UserMapper;
import application.model.User;
import application.repository.UserRepository;
import application.service.ShoppingCartService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static final Integer ONE_TIME = 1;
    private static final String CLARK_EMAIL = "clark@example.com";
    private static final String CLARK_PASSWORD = "clark123";
    private static final String CLARK_FIRST_NAME = "clark";
    private static final Long CLARK_ID = 3L;
    private static final String CLARK_LAST_NAME = "johnson";
    private static final String CLARK_SHIPPING_ADDRESS = "Shevchenko 1";
    private static final String ENCODED_PASSWORD
            = "$2a$10$nA2TJNchONFwvYeKf7kALuwncLTIoFuywvK4YMa21IrRL.pcB/645";
    private static final String EXCEPTION = "You are already registered!";
    private static User clark;
    private static UserRegistrationRequestDto clarkRequestDto;
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

    @BeforeAll
    static void beforeAll() {
        clark = new User()
                .setId(CLARK_ID)
                .setEmail(CLARK_EMAIL)
                .setFirstName(CLARK_FIRST_NAME)
                .setLastName(CLARK_LAST_NAME)
                .setShippingAddress(CLARK_SHIPPING_ADDRESS);
        clarkRequestDto = new UserRegistrationRequestDto()
                .setEmail(CLARK_EMAIL)
                .setPassword(CLARK_PASSWORD)
                .setFirstName(CLARK_FIRST_NAME)
                .setLastName(CLARK_LAST_NAME)
                .setRepeatPassword(CLARK_PASSWORD)
                .setShippingAddress(CLARK_SHIPPING_ADDRESS);
    }

    @Test
    @DisplayName("""
            Verify register() method with nonRegisteredUser
            """)
    void register_NonRegisteredUser_ReturnUserResponseDto() {
        // given
        UserResponseDto clarkResponseDto
                = new UserResponseDto()
                .setId(clark.getId())
                .setEmail(clark.getEmail())
                .setFirstName(clark.getFirstName())
                .setLastName(clark.getLastName())
                .setShippingAddress(clark.getShippingAddress());
        ShoppingCartResponseDto shoppingCartResponseDto
                = new ShoppingCartResponseDto()
                .setId(2L)
                .setUserId(clark.getId());

        // when
        when(userRepository.findUserByEmail(clarkRequestDto.getEmail()))
                .thenReturn(Optional.empty());
        when(userMapper.toModel(clarkRequestDto)).thenReturn(clark);
        when(passwordEncoder.encode(clarkRequestDto.getPassword()))
                .thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(clark)).thenReturn(clark);
        when(shoppingCartService.createShoppingCart(clark))
                .thenReturn(shoppingCartResponseDto);
        when(userMapper.toDto(clark)).thenReturn(clarkResponseDto);

        // then
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
        // when
        when(userRepository.findUserByEmail(clark.getEmail()))
                .thenReturn(Optional.ofNullable(clark));

        // then
        Exception exception
                = assertThrows(RegistrationException.class,
                    () -> userServiceImpl.register(clarkRequestDto));
        String actual = exception.getMessage();
        assertNotNull(actual);
        assertEquals(EXCEPTION, actual);
    }
}
