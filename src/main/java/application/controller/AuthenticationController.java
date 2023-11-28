package application.controller;

import application.dto.user.UserLoginRequestDto;
import application.dto.user.UserLoginResponseDto;
import application.dto.user.UserRegistrationRequestDto;
import application.dto.user.UserResponseDto;
import application.exception.RegistrationException;
import application.security.AuthenticationService;
import application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authenticate user", description = "Endpoint for maganagement users")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    @Operation(summary = "Authenticate user", description = "Endpoint for authentication")
    public UserResponseDto register(@RequestBody @Valid
                                        UserRegistrationRequestDto
                                                requestDto) throws RegistrationException {
        return userService.register(requestDto);
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Endpoint to login user")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
