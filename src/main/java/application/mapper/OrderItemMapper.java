package application.mapper;

import application.config.MapperConfig;
import application.dto.item.OrderItemResponseDto;
import application.model.Book;
import application.model.CartItem;
import application.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "id", ignore = true)
    OrderItem toOrderItem(CartItem cartItem);

    @Mapping(target = "bookId", source = "book", qualifiedByName = "getBookIdByBook")
    OrderItemResponseDto toOrderItemResponseDto(OrderItem orderItem);

    @Named("getBookIdByBook")
    default Long getBookIdByBook(Book book) {
        return book.getId();
    }
}
