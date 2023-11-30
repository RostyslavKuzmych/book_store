package application.mapper;

import application.config.MapperConfig;
import application.dto.cartItem.CartItemResponseDto;
import application.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book", qualifiedByName = "")
    CartItemResponseDto toCartItemResponseDto(CartItem cartItem);

    @Named("bookIdById")
    private Long id()
}
