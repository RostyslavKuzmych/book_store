package application.mapper;

import application.config.MapperConfig;
import application.dto.shopping.cart.ShoppingCartResponseDto;
import application.model.ShoppingCart;
import application.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {

    @Mapping(target = "userId", source = "user", qualifiedByName = "getUserId")
    ShoppingCartResponseDto toResponseDto(ShoppingCart shoppingCart);

    @Named(value = "getUserId")
    default Long getUserIdByUser(User user) {
        return user.getId();
    }
}
