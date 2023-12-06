package application.mapper;

import application.config.MapperConfig;
import application.dto.cart.item.CartItemRequestDto;
import application.dto.cart.item.CartItemResponseDto;
import application.model.Book;
import application.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {

    @Mapping(target = "bookId", source = "book", qualifiedByName = "bookIdByBook")
    @Mapping(target = "bookTitle", source = "book", qualifiedByName = "getTitleByBook")
    CartItemResponseDto toCartItemResponseDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookByBookId")
    CartItem toCartItem(CartItemRequestDto cartItemRequestDto);

    @Named(value = "bookByBookId")
    default Book bookByBookId(Long bookId) {
        Book book = new Book();
        book.setId(bookId);
        return book;
    }

    @Named(value = "bookIdByBook")
    default Long bookIdByBook(Book book) {
        return book.getId();
    }

    @Named(value = "getTitleByBook")
    default String getTitleByBook(Book book) {
        return book.getTitle();
    }
}
