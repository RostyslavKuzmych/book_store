package application.mapper;

import application.config.MapperConfig;
import application.dto.item.OrderItemResponseDto;
import application.dto.order.OrderResponseDto;
import application.model.Order;
import application.model.OrderItem;
import application.model.Status;
import application.model.User;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = {OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(target = "userId", source = "user", qualifiedByName = "getUserIdByUser")
    @Mapping(target = "status", source = "status", qualifiedByName = "toString")
    @Mapping(target = "orderItems", source = "itemSet", qualifiedByName = "getOrderItemsDtos")
    OrderResponseDto toResponseDto(Order order);

    @Named("getUserIdByUser")
    default Long getUserIdByUser(User user) {
        return user.getId();
    }

    @Named("getOrderItemsDtos")
    Set<OrderItemResponseDto> getOrderItemsDtos(Set<OrderItem> orderItems);

    @Named("toString")
    default String toString(Status status) {
        return status.toString();
    }
}
