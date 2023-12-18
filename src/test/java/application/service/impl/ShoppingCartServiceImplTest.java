package application.service.impl;

import application.dto.cart.item.CartItemRequestDto;
import application.dto.cart.item.CartItemResponseDto;
import application.dto.shopping.cart.ShoppingCartRequestDto;
import application.dto.shopping.cart.ShoppingCartResponseDto;
import application.exception.EntityNotFoundException;
import application.mapper.ShoppingCartMapper;
import application.model.Book;
import application.model.CartItem;
import application.model.ShoppingCart;
import application.model.User;
import application.repository.CartItemRepository;
import application.repository.ShoppingCartRepository;
import application.service.CartItemService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {
    private static final Integer ONE_TIME = 1;
    private static final Long INVALID_CART_ITEM_ID = 10L;
    private static final Long CART_ITEM_LOVE_IS_BOOK_ID = 1L;
    private static final Long JOHN_ID = 5L;
    private static final String FIND_CART_ITEM_EXCEPTION = "Can't find cartItem by id ";
    private static final Long ALICE_ID = 2L;
    private static final Long LOVE_IS_ID = 2L;
    private static User alice;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemService cartItemService;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ShoppingCartServiceImpl mockedShoppingCartServiceImpl;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartServiceImpl;

    @BeforeAll
    static void beforeAll() {
        alice = new User()
                .setId(ALICE_ID)
                .setEmail("alice@gmail.com")
                .setPassword("alice123")
                .setFirstName("alice")
                .setLastName("smith");
    }

    @Test
    @DisplayName("""
            Verify createShoppingCart() method with valid user object
            """)
    void createShoppingCart_ValidUser_ReturnShoppingCartDto() {
        User john = new User()
                .setId(JOHN_ID)
                .setEmail("john@gmail.com")
                .setPassword("johnson")
                .setFirstName("john")
                .setLastName("johnson");
        ShoppingCart shoppingCart = new ShoppingCart().setUser(john);
        ShoppingCartResponseDto responseDto =
                new ShoppingCartResponseDto()
                        .setId(3L)
                        .setUserId(JOHN_ID);

        when(shoppingCartRepository.save(shoppingCart)).thenReturn(shoppingCart);
        when(shoppingCartMapper.toResponseDto(shoppingCart)).thenReturn(responseDto);

        ShoppingCartResponseDto actual = shoppingCartServiceImpl.createShoppingCart(john);
        assertNotNull(actual);
        assertEquals(responseDto, actual);
        verify(shoppingCartRepository, times(ONE_TIME)).save(shoppingCart);
    }

    @Test
    @DisplayName("""
            Verify addBookToShoppingCart() method with valid inputs
            """)
    void addBookToShoppingCart_ValidInputs_ReturnShoppingCartDto() {
        CartItemRequestDto requestDto
                = new CartItemRequestDto().setQuantity(5)
                .setBookId(LOVE_IS_ID);
        ShoppingCart shoppingCart
                = new ShoppingCart().setUser(alice)
                .setId(2L);
        CartItemResponseDto cartItemResponseDto
                = new CartItemResponseDto()
                .setId(CART_ITEM_LOVE_IS_BOOK_ID)
                .setQuantity(requestDto.getQuantity())
                .setBookId(LOVE_IS_ID)
                .setBookTitle("Love is...");
        CartItem cartItem
                = new CartItem()
                .setId(CART_ITEM_LOVE_IS_BOOK_ID)
                .setBook(new Book().setId(LOVE_IS_ID))
                .setQuantity(requestDto.getQuantity())
                .setShoppingCart(shoppingCart);
        ShoppingCartResponseDto shoppingCartResponseDto
                = new ShoppingCartResponseDto().setId(2L)
                        .setUserId(ALICE_ID)
                .setCartItems(Set.of(cartItemResponseDto));

        when(shoppingCartRepository.findShoppingCartByUserId(ALICE_ID))
                .thenReturn(shoppingCart);
        when(cartItemService.createCartItem(shoppingCart, requestDto))
                .thenReturn(cartItemResponseDto);
        when(cartItemRepository.findById(CART_ITEM_LOVE_IS_BOOK_ID))
                .thenReturn(Optional.ofNullable(cartItem));
        shoppingCart.getCartItemSet().add(cartItem);
        when(shoppingCartRepository.save(shoppingCart)).thenReturn(shoppingCart);
        when(shoppingCartMapper.toResponseDto(shoppingCart)).thenReturn(shoppingCartResponseDto);

        ShoppingCartResponseDto actual
                = shoppingCartServiceImpl.addBookToShoppingCart(alice, requestDto);
        assertNotNull(actual);
        assertEquals(shoppingCartResponseDto, actual);
        verify(shoppingCartRepository, times(ONE_TIME)).findShoppingCartByUserId(ALICE_ID);
        verify(shoppingCartRepository, times(ONE_TIME)).save(shoppingCart);
    }

    @Test
    @DisplayName("""
            Verify getShoppingCartDto() method with correct userId
            """)
    void getShoppingCartDto_ValidUserId_ReturnShoppingCartDto() {
        ShoppingCart shoppingCart
                = new ShoppingCart()
                .setId(5L)
                .setUser(alice);
        ShoppingCartResponseDto responseDto
                = new ShoppingCartResponseDto()
                .setId(shoppingCart.getId())
                .setUserId(ALICE_ID);

        when(shoppingCartRepository.findShoppingCartByUserId(ALICE_ID))
                .thenReturn(shoppingCart);
        when(shoppingCartMapper.toResponseDto(shoppingCart))
                .thenReturn(responseDto);

        ShoppingCartResponseDto actual = shoppingCartServiceImpl.getShoppingCartDto(ALICE_ID);
        assertNotNull(actual);
        assertEquals(responseDto, actual);
        verify(shoppingCartRepository,
                times(ONE_TIME)).findShoppingCartByUserId(ALICE_ID);
    }

    @Test
    @DisplayName("""
            Verify clearShoppingCart() method
            """)
    void clearShoppingCart_MethodCall_ReturnEmptyShoppingCartDto() {
        ShoppingCart shoppingCart
                = new ShoppingCart()
                .setId(4L)
                .setUser(alice)
                .setCartItemSet(Set.of(new CartItem().setId(3L), new CartItem().setId(4L)));
        ShoppingCart clearedShoppingCart
                = new ShoppingCart()
                .setId(shoppingCart.getId())
                .setUser(alice);
        ShoppingCartResponseDto responseDto
                = new ShoppingCartResponseDto()
                .setId(shoppingCart.getId())
                .setUserId(ALICE_ID);

        when(shoppingCartRepository.save(clearedShoppingCart))
                .thenReturn(clearedShoppingCart);
        when(shoppingCartMapper.toResponseDto(clearedShoppingCart))
                .thenReturn(responseDto);

        ShoppingCartResponseDto actual
                = shoppingCartServiceImpl.clearShoppingCart(shoppingCart);
        assertNotNull(actual);
        assertEquals(responseDto, actual);
        verify(shoppingCartRepository, times(ONE_TIME))
                .save(clearedShoppingCart);
    }

    @Test
    @DisplayName("""
            Verify updateQuantityById() method with correct cartItemId
            """)
    void updateQuantityById_ValidCartItemId_ReturnShoppingCartDto() {
        Long inputId = 3L;
        ShoppingCartRequestDto shoppingCartRequestDto
                = new ShoppingCartRequestDto().setQuantity(5);
        CartItem theHobbitCartItem
                = new CartItem()
                .setId(2L)
                .setQuantity(4)
                .setBook(new Book().setId(2L).setTitle("The hobbit"));
        CartItem theLittlePrinceCartItem
                = new CartItem()
                .setId(3L)
                .setQuantity(10)
                .setBook(new Book().setId(3L).setTitle("The little prince"));
        CartItemResponseDto theHobbitCartItemDto
                = new CartItemResponseDto()
                .setId(theHobbitCartItem.getId())
                .setQuantity(theHobbitCartItem.getQuantity())
                .setBookId(theHobbitCartItem.getBook().getId());
        CartItemResponseDto theLittlePrinceCartItemDto
                = new CartItemResponseDto()
                .setId(theLittlePrinceCartItem.getId())
                .setQuantity(shoppingCartRequestDto.getQuantity())
                .setBookId(theLittlePrinceCartItem.getBook().getId());
        ShoppingCart shoppingCart
                = new ShoppingCart()
                .setId(4L)
                .setUser(alice)
                .setCartItemSet(Set.of(theHobbitCartItem, theLittlePrinceCartItem));
        ShoppingCartResponseDto responseDto
                = new ShoppingCartResponseDto()
                .setId(shoppingCart.getId())
                .setUserId(ALICE_ID)
                .setCartItems(Set.of(theHobbitCartItemDto, theLittlePrinceCartItemDto));

        when(cartItemRepository.findById(inputId))
                .thenReturn(Optional.ofNullable(theLittlePrinceCartItem));
        theLittlePrinceCartItem.setQuantity(shoppingCartRequestDto.getQuantity());
        Mockito.lenient().when(cartItemRepository.save(theLittlePrinceCartItem))
                .thenReturn(theLittlePrinceCartItem);
        when(shoppingCartRepository.findShoppingCartByUserId(ALICE_ID))
                .thenReturn(shoppingCart);
        when(shoppingCartRepository.save(shoppingCart))
                .thenReturn(shoppingCart);
        when(shoppingCartMapper.toResponseDto(shoppingCart))
                .thenReturn(responseDto);

        ShoppingCartResponseDto actual
                = shoppingCartServiceImpl.updateQuantityById(alice, inputId, shoppingCartRequestDto);
        assertNotNull(actual);
        assertEquals(responseDto, actual);
        verify(shoppingCartRepository, times(ONE_TIME))
                .findShoppingCartByUserId(ALICE_ID);
        verify(shoppingCartRepository, times(ONE_TIME))
                .save(shoppingCart);
    }

    @Test
    @DisplayName("""
            Verify throwing exception with invalid cartItemId
            """)
    void updateQuantityById_InvalidCartItemId_ThrowException() {
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartServiceImpl.updateQuantityById(alice,
                        INVALID_CART_ITEM_ID, new ShoppingCartRequestDto().setQuantity(3)));
        String expected = FIND_CART_ITEM_EXCEPTION + INVALID_CART_ITEM_ID;
        String actual = exception.getMessage();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}
