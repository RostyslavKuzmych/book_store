package application.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {
    private static final Integer ONE_TIME = 1;
    private static final Long INPUT_ID = 3L;
    private static final String THE_HOBBIT = "The hobbit";
    private static final String THE_LITTLE_PRINCE = "The little prince";
    private static final String ALICE_EMAIL = "alice@example.com";
    private static final String ALICE_PASSWORD = "alice123";
    private static final String ALICE_FIRST_NAME = "alice";
    private static final String ALICE_LAST_NAME = "smith";
    private static final String JOHN_EMAIL = "john@example.com";
    private static final String JOHN_PASSWORD = "john123";
    private static final String JOHN_FIRST_NAME = "john";
    private static final String JOHN_LAST_NAME = "johnson";
    private static final Long INVALID_CART_ITEM_ID = 10L;
    private static final Long CART_ITEM_LOVE_IS_BOOK_ID = 1L;
    private static final String LOVE_IS_TITLE = "Love is...";
    private static final Long JOHN_ID = 5L;
    private static final String FIND_CART_ITEM_EXCEPTION = "Can't find cartItem by id ";
    private static final Long ALICE_ID = 2L;
    private static final Long LOVE_IS_ID = 2L;
    private static User alice;
    private static User john;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemService cartItemService;
    @Mock
    private CartItemRepository cartItemRepository;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartServiceImpl;

    @BeforeAll
    static void beforeAll() {
        alice = new User()
                .setId(ALICE_ID)
                .setEmail(ALICE_EMAIL)
                .setPassword(ALICE_PASSWORD)
                .setFirstName(ALICE_FIRST_NAME)
                .setLastName(ALICE_LAST_NAME);
        john = new User()
                .setId(JOHN_ID)
                .setEmail(JOHN_EMAIL)
                .setPassword(JOHN_PASSWORD)
                .setFirstName(JOHN_FIRST_NAME)
                .setLastName(JOHN_LAST_NAME);
    }

    @Test
    @DisplayName("""
            Verify createShoppingCart() method with valid user object
            """)
    void createShoppingCart_ValidUser_ReturnShoppingCartDto() {
        // given
        ShoppingCart shoppingCart = new ShoppingCart()
                .setId(3L)
                .setUser(john);
        ShoppingCartResponseDto responseDto =
                new ShoppingCartResponseDto()
                        .setId(shoppingCart.getId())
                        .setUserId(shoppingCart.getUser().getId());

        // when
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(shoppingCart);
        when(shoppingCartMapper.toResponseDto(shoppingCart)).thenReturn(responseDto);

        // then
        ShoppingCartResponseDto actual = shoppingCartServiceImpl.createShoppingCart(john);
        assertNotNull(actual);
        assertEquals(responseDto, actual);
        verify(shoppingCartRepository, times(ONE_TIME)).save(any(ShoppingCart.class));
    }

    @Test
    @DisplayName("""
            Verify addBookToShoppingCart() method with valid inputs
            """)
    void addBookToShoppingCart_ValidInputs_ReturnShoppingCartDto() {
        // given
        ShoppingCart shoppingCart
                = new ShoppingCart()
                .setId(1L)
                .setUser(alice);
        CartItemRequestDto requestDto
                = new CartItemRequestDto()
                .setQuantity(5)
                .setBookId(LOVE_IS_ID);
        CartItem cartItem
                = new CartItem()
                .setId(CART_ITEM_LOVE_IS_BOOK_ID)
                .setBook(new Book().setId(requestDto.getBookId()))
                .setQuantity(requestDto.getQuantity())
                .setShoppingCart(shoppingCart);
        ShoppingCart shoppingCartWithCartItems
                = new ShoppingCart()
                .setId(shoppingCart.getId())
                .setUser(shoppingCart.getUser())
                .setCartItemSet(Set.of(cartItem));
        CartItemResponseDto cartItemResponseDto
                = new CartItemResponseDto()
                .setId(cartItem.getId())
                .setQuantity(cartItem.getQuantity())
                .setBookId(cartItem.getBook().getId())
                .setBookTitle(LOVE_IS_TITLE);
        ShoppingCartResponseDto shoppingCartResponseDto
                = new ShoppingCartResponseDto()
                .setId(shoppingCartWithCartItems.getId())
                .setUserId(shoppingCartWithCartItems.getUser().getId())
                .setCartItems(Set.of(cartItemResponseDto));

        // when
        when(shoppingCartRepository.getShoppingCartByUserId(ALICE_ID))
                .thenReturn(shoppingCart);
        when(cartItemService.createCartItem(shoppingCart, requestDto))
                .thenReturn(cartItemResponseDto);
        when(cartItemRepository.findById(cartItemResponseDto.getId()))
                .thenReturn(Optional.ofNullable(cartItem));
        when(shoppingCartRepository.save(shoppingCartWithCartItems))
                .thenReturn(shoppingCartWithCartItems);
        when(shoppingCartMapper.toResponseDto(shoppingCartWithCartItems))
                .thenReturn(shoppingCartResponseDto);

        // then
        ShoppingCartResponseDto actual
                = shoppingCartServiceImpl.addBookToShoppingCart(alice, requestDto);
        assertNotNull(actual);
        assertEquals(shoppingCartResponseDto, actual);
        verify(shoppingCartRepository, times(ONE_TIME)).getShoppingCartByUserId(ALICE_ID);
        verify(shoppingCartRepository, times(ONE_TIME)).save(shoppingCartWithCartItems);
    }

    @Test
    @DisplayName("""
            Verify getShoppingCartDto() method with correct userId
            """)
    void getShoppingCartDto_ValidUserId_ReturnShoppingCartDto() {
        // given
        ShoppingCart shoppingCart
                = new ShoppingCart()
                .setId(1L)
                .setUser(alice);
        ShoppingCartResponseDto shoppingCartResponseDto
                = new ShoppingCartResponseDto()
                .setId(shoppingCart.getId())
                .setUserId(shoppingCart.getUser().getId());

        // when
        when(shoppingCartRepository.getShoppingCartByUserId(ALICE_ID))
                .thenReturn(shoppingCart);
        when(shoppingCartMapper.toResponseDto(shoppingCart))
                .thenReturn(shoppingCartResponseDto);

        // then
        ShoppingCartResponseDto actual = shoppingCartServiceImpl.getShoppingCartDto(ALICE_ID);
        assertNotNull(actual);
        assertEquals(shoppingCartResponseDto, actual);
        verify(shoppingCartRepository,
                times(ONE_TIME)).getShoppingCartByUserId(ALICE_ID);
    }

    @Test
    @DisplayName("""
            Verify clearShoppingCart() method
            """)
    void clearShoppingCart_MethodCall_ReturnEmptyShoppingCartDto() {
        // given
        ShoppingCart shoppingCart
                = new ShoppingCart()
                .setId(1L)
                .setUser(alice)
                .setCartItemSet(Set.of(new CartItem().setId(3L), new CartItem().setId(4L)));
        ShoppingCart clearedShoppingCart
                = new ShoppingCart()
                .setId(shoppingCart.getId())
                .setUser(shoppingCart.getUser());
        ShoppingCartResponseDto shoppingCartResponseDto
                = new ShoppingCartResponseDto()
                .setId(clearedShoppingCart.getId())
                .setUserId(clearedShoppingCart.getUser().getId());

        // when
        when(shoppingCartRepository.save(clearedShoppingCart))
                .thenReturn(clearedShoppingCart);
        when(shoppingCartMapper.toResponseDto(clearedShoppingCart))
                .thenReturn(shoppingCartResponseDto);

        // then
        ShoppingCartResponseDto actual
                = shoppingCartServiceImpl.clearShoppingCart(shoppingCart);
        assertNotNull(actual);
        assertEquals(shoppingCartResponseDto, actual);
        verify(shoppingCartRepository, times(ONE_TIME))
                .save(clearedShoppingCart);
    }

    @Test
    @DisplayName("""
            Verify updateQuantityById() method with correct cartItemId
            """)
    void updateQuantityById_ValidCartItemId_ReturnShoppingCartDto() {
        // given
        ShoppingCartRequestDto shoppingCartRequestDto
                = new ShoppingCartRequestDto().setQuantity(5);
        CartItem theHobbitCartItem
                = new CartItem()
                .setId(2L)
                .setQuantity(4)
                .setBook(new Book().setId(2L).setTitle(THE_HOBBIT));
        CartItem theLittlePrinceCartItem
                = new CartItem()
                .setId(3L)
                .setQuantity(10)
                .setBook(new Book().setId(3L).setTitle(THE_LITTLE_PRINCE));
        ShoppingCart shoppingCart
                = new ShoppingCart()
                .setId(4L)
                .setUser(alice)
                .setCartItemSet(Set.of(theHobbitCartItem, theLittlePrinceCartItem));
        theLittlePrinceCartItem.setQuantity(shoppingCartRequestDto.getQuantity());
        ShoppingCart updatedShoppingCart
                = new ShoppingCart()
                .setId(4L)
                .setUser(alice)
                .setCartItemSet(Set.of(theHobbitCartItem, theLittlePrinceCartItem));
        CartItemResponseDto theHobbitCartItemDto
                = new CartItemResponseDto()
                .setId(theHobbitCartItem.getId())
                .setQuantity(theHobbitCartItem.getQuantity())
                .setBookId(theHobbitCartItem.getBook().getId());
        CartItemResponseDto theLittlePrinceCartItemDto
                = new CartItemResponseDto()
                .setId(theLittlePrinceCartItem.getId())
                .setQuantity(theLittlePrinceCartItem.getQuantity())
                .setBookId(theLittlePrinceCartItem.getBook().getId());
        ShoppingCartResponseDto shoppingCartResponseDto
                = new ShoppingCartResponseDto()
                .setId(shoppingCart.getId())
                .setUserId(shoppingCart.getUser().getId())
                .setCartItems(Set.of(theHobbitCartItemDto, theLittlePrinceCartItemDto));

        // when
        when(cartItemRepository.findById(INPUT_ID))
                .thenReturn(Optional.ofNullable(theLittlePrinceCartItem));
        Mockito.lenient().when(cartItemRepository.save(theLittlePrinceCartItem))
                .thenReturn(theLittlePrinceCartItem);
        when(shoppingCartRepository.getShoppingCartByUserId(ALICE_ID))
                .thenReturn(shoppingCart);
        when(shoppingCartRepository.save(updatedShoppingCart))
                .thenReturn(updatedShoppingCart);
        when(shoppingCartMapper.toResponseDto(updatedShoppingCart))
                .thenReturn(shoppingCartResponseDto);

        // then
        ShoppingCartResponseDto actual
                = shoppingCartServiceImpl
                .updateQuantityById(alice, INPUT_ID, shoppingCartRequestDto);
        assertNotNull(actual);
        assertEquals(shoppingCartResponseDto, actual);
        verify(shoppingCartRepository, times(ONE_TIME))
                .getShoppingCartByUserId(ALICE_ID);
        verify(shoppingCartRepository, times(ONE_TIME))
                .save(updatedShoppingCart);
    }

    @Test
    @DisplayName("""
            Verify throwing exception with invalid cartItemId
            """)
    void updateQuantityById_InvalidCartItemId_ThrowException() {
        // given
        ShoppingCartRequestDto shoppingCartRequestDto
                = new ShoppingCartRequestDto().setQuantity(3);

        // when
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartServiceImpl.updateQuantityById(alice,
                        INVALID_CART_ITEM_ID, shoppingCartRequestDto));

        // then
        String expected = FIND_CART_ITEM_EXCEPTION + INVALID_CART_ITEM_ID;
        String actual = exception.getMessage();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}
