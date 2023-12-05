package application.mapper;

import application.config.MapperConfig;
import application.dto.order.OrderResponseDto;
import application.model.Order;
import application.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user", qualifiedByName = "getUserIdByUser")
    @Mapping(target = "status", source = "status", qualifiedByName = "toString")
    OrderResponseDto toResponseDto(Order order);

    @Named("getUserIdByUser")
    default Long getUserIdByUser(User user) {
        return user.getId();
    }

    @Named("toString")
    default String toString(Order.Status status) {
        return status.toString();
    }
}
