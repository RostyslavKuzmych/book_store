package application.mapper;

import application.config.MapperConfig;
import application.dto.cart.item.CartItemResponseDto;
import application.dto.shopping.cart.ShoppingCartResponseDto;
import application.model.CartItem;
import application.model.ShoppingCart;
import application.model.User;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {

    @Mapping(target = "userId", source = "user", qualifiedByName = "getUserId")
    @Mapping(target = "cartItems", source = "cartItemSet", qualifiedByName = "getCartItemsDtos")
    ShoppingCartResponseDto toResponseDto(ShoppingCart shoppingCart);

    @Named(value = "getUserId")
    default Long getUserIdByUser(User user) {
        return user.getId();
    }

    @Named(value = "getCartItemsDtos")
    Set<CartItemResponseDto> getCartItemsDto(Set<CartItem> cartItemSet);
}
