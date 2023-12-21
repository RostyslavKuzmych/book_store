package application.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import application.dto.cart.item.CartItemRequestDto;
import application.dto.cart.item.CartItemResponseDto;
import application.dto.shopping.cart.ShoppingCartRequestDto;
import application.dto.shopping.cart.ShoppingCartResponseDto;
import application.model.Role;
import application.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;
    private static final String BASE_URL = "/api/cart";
    private static final String PATH_CART_ITEMS = "classpath:database/cart_items/";
    private static final Long ALICE_ID = 2L;
    private static final String RECENT_ADDED_CART_ITEM_ID = "/cart-items/6";
    private static final String ALICE_EMAIL = "alice@example.com";
    private static final String ALICE_PASSWORD = "12345678";
    private static CartItemResponseDto smallCartItemDto;
    private static CartItemResponseDto bigCartItemDto;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        smallCartItemDto = new CartItemResponseDto()
                .setId(3L)
                .setBookId(2L)
                .setBookTitle("Pride and Prejudice")
                .setQuantity(12);
        bigCartItemDto = new CartItemResponseDto()
                .setId(4L)
                .setBookTitle("1984")
                .setBookId(3L)
                .setQuantity(10);
    }

    @BeforeEach
    void setUp() {
        User alice = new User();
        alice.setId(ALICE_ID);
        alice.setEmail(ALICE_EMAIL);
        alice.setPassword(ALICE_PASSWORD);
        alice.setRoleSet(Set.of(new Role().setId(1L).setRolename(Role.Rolename.USER)));
        Authentication authentication
                = new UsernamePasswordAuthenticationToken(alice,
                alice.getPassword(), alice.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @Sql(scripts = PATH_CART_ITEMS + "save_other_two_cartItems_to_cart_items_table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = PATH_CART_ITEMS + "remove_other_two_cartItems_from_cart_items_table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = PATH_CART_ITEMS + "remove_cartItem_from_cart_items_table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", roles = ControllerTestUtil.USER)
    @DisplayName("""
            Verify addCartItemToShoppingCart() method with correct cartItemRequest
            """)
    void addCartItemToShoppingCart_ValidCartItemRequest_ReturnShoppingCartDto() throws Exception {
        // given
        CartItemRequestDto cartItemRequestDto
                = new CartItemRequestDto()
                .setBookId(3L)
                .setQuantity(20);
        CartItemResponseDto cartItemResponseDto
                = new CartItemResponseDto()
                .setId(5L)
                .setBookTitle("1984")
                .setBookId(cartItemRequestDto.getBookId())
                .setQuantity(cartItemRequestDto.getQuantity());
        ShoppingCartResponseDto shoppingCartResponseDto
                = new ShoppingCartResponseDto()
                .setId(1L)
                .setUserId(ALICE_ID)
                .setCartItems(Set.of(smallCartItemDto, bigCartItemDto, cartItemResponseDto));
        String jsonRequest = objectMapper.writeValueAsString(cartItemRequestDto);

        // when
        MvcResult mvcResult = mockMvc.perform(post(BASE_URL)
                        .content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // then
        ShoppingCartResponseDto actual
                = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                ShoppingCartResponseDto.class);
        assertNotNull(actual);
        assertEquals(shoppingCartResponseDto, actual);
    }

    @Test
    @Sql(scripts = PATH_CART_ITEMS + "save_other_two_cartItems_to_cart_items_table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = PATH_CART_ITEMS + "remove_other_two_cartItems_from_cart_items_table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", roles = ControllerTestUtil.USER)
    @DisplayName("""
            Verify getShoppingCart() method with correct userId
            """)
    void getShoppingCart_ValidUserId_ReturnShoppingCartDto() throws Exception {
        // given
        ShoppingCartResponseDto shoppingCartResponseDto = new ShoppingCartResponseDto()
                .setId(1L)
                .setUserId(ALICE_ID)
                .setCartItems(Set.of(smallCartItemDto, bigCartItemDto));

        // when
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL)).andReturn();

        // then
        ShoppingCartResponseDto actual
                = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                ShoppingCartResponseDto.class);
        assertNotNull(actual);
        assertEquals(shoppingCartResponseDto, actual);
    }

    @Test
    @DisplayName("""
            Verify updateQuantityById() method with correct shoppingCartRequest
            """)
    @WithMockUser(username = "user", roles = ControllerTestUtil.USER)
    @Sql(scripts = PATH_CART_ITEMS + "save_cartItem_to_cart_items_table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = PATH_CART_ITEMS + "remove_recent_added_cartItem_from_cart_items_table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateQuantityById_ValidShoppingCartRequest_ReturnShoppingCartDto() throws Exception {
        // given
        ShoppingCartRequestDto shoppingCartRequestDto
                = new ShoppingCartRequestDto()
                .setQuantity(12);
        CartItemResponseDto cartItemResponseDto
                = new CartItemResponseDto()
                .setId(6L)
                .setBookId(3L)
                .setBookTitle("1984")
                .setQuantity(shoppingCartRequestDto.getQuantity());
        ShoppingCartResponseDto shoppingCartResponseDto
                = new ShoppingCartResponseDto()
                .setId(1L)
                .setUserId(ALICE_ID)
                .setCartItems(Set.of(cartItemResponseDto));
        String jsonRequest = objectMapper.writeValueAsString(shoppingCartRequestDto);

        // when
        MvcResult mvcResult = mockMvc.perform(put(BASE_URL + RECENT_ADDED_CART_ITEM_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        // then
        ShoppingCartResponseDto actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                        ShoppingCartResponseDto.class);
        assertNotNull(actual);
        assertEquals(shoppingCartResponseDto, actual);
    }
}
