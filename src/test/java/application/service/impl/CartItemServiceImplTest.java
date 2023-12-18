package application.service.impl;

import application.dto.book.BookDto;
import application.dto.cart.item.CartItemRequestDto;
import application.dto.cart.item.CartItemResponseDto;
import application.mapper.BookMapper;
import application.mapper.CartItemMapper;
import application.model.Book;
import application.model.CartItem;
import application.model.ShoppingCart;
import application.model.User;
import application.repository.CartItemRepository;
import application.service.BookService;
import jakarta.inject.Named;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartItemServiceImplTest {
    private static final Long CART_ITEM_LOVE_IS_BOOK_ID = 2L;
    private static final Integer ONE_TIME = 1;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private BookService bookService;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private CartItemServiceImpl cartItemServiceImpl;

    @Test
    @DisplayName("""
            Verify findById() method with correct cartItemId
            """)
    void findById_ValidCartItemId_ReturnCartItemDto() {
        CartItem cartItem
                = new CartItem()
                .setId(CART_ITEM_LOVE_IS_BOOK_ID)
                .setBook(new Book().setId(1L).setTitle("Love is..."))
                .setShoppingCart(new ShoppingCart().setId(2L))
                .setQuantity(5);
        CartItemResponseDto cartItemResponseDto
                = new CartItemResponseDto()
                .setId(CART_ITEM_LOVE_IS_BOOK_ID)
                .setBookId(cartItem.getBook().getId())
                .setQuantity(cartItem.getQuantity())
                .setBookTitle(cartItem.getBook().getTitle());

        when(cartItemRepository.findById(CART_ITEM_LOVE_IS_BOOK_ID))
                .thenReturn(Optional.ofNullable(cartItem));
        when(cartItemMapper.toCartItemResponseDto(cartItem))
                .thenReturn(cartItemResponseDto);

        CartItemResponseDto actual
                = cartItemServiceImpl.findById(CART_ITEM_LOVE_IS_BOOK_ID);
        assertNotNull(actual);
        assertEquals(cartItemResponseDto, actual);
        verify(cartItemRepository, times(ONE_TIME)).findById(CART_ITEM_LOVE_IS_BOOK_ID);
    }
    @Test
    @DisplayName("""
            Verify createCartItem() with correct requestDto
            """)
    void createCartItem_ValidRequestDto_ReturnCartItemDto() {
        Book book1984
                = new Book()
                .setId(3L)
                .setTitle("1984")
                .setPrice(BigDecimal.valueOf(16));
        BookDto book1984Dto
                = new BookDto()
                .setId(book1984.getId())
                .setTitle(book1984.getTitle())
                .setPrice(book1984.getPrice());
        ShoppingCart shoppingCart
                = new ShoppingCart()
                .setId(1L)
                .setUser(new User().setId(1L).setEmail("alice@gmail.com"));
        CartItemRequestDto cartItemRequestDto
                = new CartItemRequestDto()
                .setBookId(book1984.getId())
                .setQuantity(10);
        CartItem cartItem
                = new CartItem()
                .setId(2L)
                .setBook(new Book().setId(cartItemRequestDto.getBookId()))
                .setQuantity(cartItemRequestDto.getQuantity());
        CartItemResponseDto cartItemResponseDto
                = new CartItemResponseDto()
                .setId(cartItem.getId())
                .setBookTitle("1984")
                .setBookId(cartItem.getBook().getId())
                .setQuantity(cartItem.getQuantity());

        when(cartItemMapper.toCartItem(cartItemRequestDto)).thenReturn(cartItem);
        when(bookService.getBookDtoById(cartItem.getBook().getId()))
                .thenReturn(book1984Dto);
        when(bookMapper.toModelFromDto(book1984Dto))
                .thenReturn(book1984);
        cartItem.setBook(book1984);
        cartItem.setShoppingCart(shoppingCart);
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(cartItemMapper.toCartItemResponseDto(cartItem)).thenReturn(cartItemResponseDto);

        CartItemResponseDto actual
                = cartItemServiceImpl.createCartItem(shoppingCart, cartItemRequestDto);
        assertNotNull(actual);
        assertEquals(cartItemResponseDto, actual);
        verify(cartItemRepository, times(ONE_TIME)).save(cartItem);
    }
}