package application.dto.user;

import application.validation.Email;

public record UserLoginRequestDto(@Email String email, String password) {
}
