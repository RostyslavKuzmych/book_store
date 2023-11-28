package application.mapper;

import application.config.MapperConfig;
import application.dto.user.UserRegistrationRequestDto;
import application.dto.user.UserResponseDto;
import application.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto userRequestDto);
}
