package application.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import application.dto.item.OrderItemResponseDto;
import application.dto.order.OrderRequestShippingAddressDto;
import application.dto.order.OrderResponseDto;
import application.model.Role;
import application.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@Sql(scripts = "classpath:database/orders/save_three_orders_to_orders_table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/order_items/save_three_orderItems_to_order_items_table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/order_items/remove_three_orderItems_from_order_items_table.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "classpath:database/orders/remove_three_orders_from_orders_table.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {
    protected static MockMvc mockMvc;
    private static final String SPECIFIC_ORDER_ITEM = "/1/items/3";
    private static final String ALL_ORDER_ITEMS = "/2/items";
    private static final String ALICE_EMAIL = "alice@example.com";
    private static final String ALICE_PASSWORD = "12345678";
    private static final String BASE_URL = "/api/orders";
    private static final Long ALICE_ID = 2L;
    private static final String PATH_CART_ITEMS = "classpath:database/cart_items/";
    private static final String PATH_ORDER_ITEMS = "classpath:database/order_items/";
    private static final String PATH_ORDERS = "classpath:database/orders/";
    private static final String ORDER_DATE = "orderDate";
    private static OrderItemResponseDto smallOrderItemDto;
    private static OrderItemResponseDto bigOrderItemDto;
    private static OrderItemResponseDto veryBigOrderItemDto;

    @Autowired
    private ObjectMapper objectMapper;

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

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        smallOrderItemDto
                = new OrderItemResponseDto()
                .setId(1L)
                .setBookId(1L)
                .setQuantity(5);
        bigOrderItemDto
                = new OrderItemResponseDto()
                .setId(2L)
                .setBookId(2L)
                .setQuantity(7);
        veryBigOrderItemDto
                = new OrderItemResponseDto()
                .setId(3L)
                .setBookId(3L)
                .setQuantity(20);
    }

    @Test
    @Sql(scripts = PATH_CART_ITEMS + "save_two_cartItems_to_cart_items_table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = PATH_ORDER_ITEMS + "remove_two_orderItems_from_order_items_table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = PATH_ORDERS + "remove_alice_order_from_orders_table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", roles = ControllerTestUtil.USER)
    @DisplayName("""
            Verify placeOrder() method with correct orderRequest
            """)
    void placeOrder_ValidOrderRequest_ReturnOrderDto() throws Exception {
        // given
        OrderRequestShippingAddressDto shippingAddressDto
                = new OrderRequestShippingAddressDto()
                .setShippingAddress("Shevchenko 123A");
        OrderItemResponseDto smallOrderItemDto
                = new OrderItemResponseDto()
                .setId(4L)
                .setBookId(1L)
                .setQuantity(5);
        OrderItemResponseDto bigOrderItemDto
                = new OrderItemResponseDto()
                .setId(5L)
                .setBookId(2L)
                .setQuantity(8);
        OrderResponseDto orderResponseDto
                = new OrderResponseDto()
                .setId(4L)
                .setStatus("RECEIVED")
                .setTotal(BigDecimal.valueOf(215))
                .setUserId(ALICE_ID)
                .setOrderItems(Set.of(smallOrderItemDto, bigOrderItemDto));
        String jsonRequest
                = objectMapper.writeValueAsString(shippingAddressDto);

        // when
        MvcResult mvcResult = mockMvc.perform(post(BASE_URL)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // then
        OrderResponseDto actual
                = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                OrderResponseDto.class);
        assertNotNull(actual.getOrderDate());
        EqualsBuilder.reflectionEquals(orderResponseDto, actual, ORDER_DATE);
    }

    @Test
    @WithMockUser(username = "user", roles = ControllerTestUtil.USER)
    @DisplayName("""
            Verify getAllOrders() method with correct userId
            """)
    void getAllOrders_ValidUserId_ReturnTwoOrders() throws Exception {
        // given
        OrderResponseDto firstAliceOrderDto = new OrderResponseDto()
                .setId(1L)
                .setTotal(BigDecimal.valueOf(180))
                .setStatus("RECEIVED")
                .setUserId(ALICE_ID)
                .setOrderDate(LocalDateTime.of(2023, 12,
                        17, 2, 0, 0))
                .setOrderItems(Set.of(veryBigOrderItemDto));
        OrderResponseDto secondAliceOrderDto = new OrderResponseDto()
                .setId(2L)
                .setTotal(BigDecimal.valueOf(195))
                .setUserId(ALICE_ID)
                .setStatus("RECEIVED")
                .setOrderDate(LocalDateTime.of(2023, 12,
                        17, 3, 0, 0))
                .setOrderItems(Set.of(smallOrderItemDto, bigOrderItemDto));

        // when
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk()).andReturn();

        // then
        List<OrderResponseDto> expected = List.of(firstAliceOrderDto, secondAliceOrderDto);
        List<OrderResponseDto> actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                        new TypeReference<List<OrderResponseDto>>() {
                    });
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user", roles = ControllerTestUtil.USER)
    @DisplayName("""
            Verify getAllOrderItemsByOrderId() method with correct orderId
            """)
    void getAllOrderItemsByOrderId_ValidOrderId_ReturnTwoOrderItems() throws Exception {
        // when
        MvcResult mvcResult =
                mockMvc.perform(get(BASE_URL + ALL_ORDER_ITEMS))
                        .andExpect(status().isOk()).andReturn();

        // then
        List<OrderItemResponseDto> expected = List.of(smallOrderItemDto, bigOrderItemDto);
        List<OrderItemResponseDto> actual
                = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                    new TypeReference<List<OrderItemResponseDto>>() {
                    });
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user", roles = ControllerTestUtil.USER)
    @DisplayName("""
            Verify getSpecificOrderItem() method with correct orderItemId
            """)
    void getSpecificOrderItem_ValidOrderItemId_ReturnOrderItemDto() throws Exception {
        // when
        MvcResult mvcResult =
                mockMvc.perform(get(BASE_URL + SPECIFIC_ORDER_ITEM))
                        .andExpect(status().isOk()).andReturn();

        // then
        OrderItemResponseDto actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                        OrderItemResponseDto.class);
        assertNotNull(actual);
        assertEquals(veryBigOrderItemDto, actual);
    }
}
