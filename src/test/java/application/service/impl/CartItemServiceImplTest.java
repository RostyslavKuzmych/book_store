package application.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartItemServiceImplTest {
    private static final Long CART_ITEM_LOVE_IS_BOOK_ID = 2L;
    private static final Integer ONE_TIME = 1;
    private static final String BOOK_1984 = "1984";
    private static final String ALICE_EMAIL = "alice@gmail.com";
    private static final Long ALICE_ID = 2L;
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
        // given
        CartItem cartItem
                = new CartItem()
                .setId(CART_ITEM_LOVE_IS_BOOK_ID)
                .setBook(new Book().setId(1L).setTitle("Love is..."))
                .setShoppingCart(new ShoppingCart().setId(2L))
                .setQuantity(5);
        CartItemResponseDto cartItemResponseDto
                = new CartItemResponseDto()
                .setId(cartItem.getId())
                .setBookId(cartItem.getBook().getId())
                .setQuantity(cartItem.getQuantity())
                .setBookTitle(cartItem.getBook().getTitle());

        // when
        when(cartItemRepository.findById(CART_ITEM_LOVE_IS_BOOK_ID))
                .thenReturn(Optional.ofNullable(cartItem));
        when(cartItemMapper.toCartItemResponseDto(cartItem))
                .thenReturn(cartItemResponseDto);

        // then
        CartItemResponseDto actual
                = cartItemServiceImpl.findById(CART_ITEM_LOVE_IS_BOOK_ID);
        assertNotNull(actual);
        assertEquals(cartItemResponseDto, actual);
        verify(cartItemRepository, times(ONE_TIME)).findById(CART_ITEM_LOVE_IS_BOOK_ID);
    }

    @Test
    @DisplayName("""
            Verify createCartItem() with correct cartItemRequest
            """)
    void createCartItem_ValidCartItemRequest_ReturnCartItemDto() {
        // given
        Book book1984
                = new Book()
                .setId(3L)
                .setTitle(BOOK_1984)
                .setPrice(BigDecimal.valueOf(16));
        BookDto book1984Dto
                = new BookDto()
                .setId(book1984.getId())
                .setTitle(book1984.getTitle())
                .setPrice(book1984.getPrice());
        ShoppingCart shoppingCart
                = new ShoppingCart()
                .setId(1L)
                .setUser(new User().setId(ALICE_ID).setEmail(ALICE_EMAIL));
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
                .setBookTitle(book1984.getTitle())
                .setBookId(cartItem.getBook().getId())
                .setQuantity(cartItem.getQuantity());

        // when
        when(cartItemMapper.toCartItem(cartItemRequestDto)).thenReturn(cartItem);
        when(bookService.getBookDtoById(cartItem.getBook().getId()))
                .thenReturn(book1984Dto);
        when(bookMapper.toModelFromDto(book1984Dto))
                .thenReturn(book1984);
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(cartItemMapper.toCartItemResponseDto(cartItem)).thenReturn(cartItemResponseDto);

        // then
        CartItemResponseDto actual
                = cartItemServiceImpl.createCartItem(shoppingCart, cartItemRequestDto);
        assertNotNull(actual);
        assertEquals(cartItemResponseDto, actual);
        verify(cartItemRepository, times(ONE_TIME)).save(cartItem);
    }
}
