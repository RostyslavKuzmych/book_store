package application.mapper;

import application.config.MapperConfig;
import application.dto.user.UserRegistrationRequestDto;
import application.dto.user.UserResponseDto;
import application.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    @Mapping(target = "password", ignore = true)
    User toModel(UserRegistrationRequestDto userRequestDto);
}
