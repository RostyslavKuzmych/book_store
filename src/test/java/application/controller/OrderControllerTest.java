package application.controller;

import application.dto.item.OrderItemResponseDto;
import application.dto.order.OrderRequestShippingAddressDto;
import application.dto.order.OrderResponseDto;
import application.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {
    protected static MockMvc mockMvc;
    private static final String BASE_URL = "/api/orders";
    private static final String PATH_CART_ITEMS = "classpath:database/cart_items/";
    private static final String PATH_ORDER_ITEMS = "classpath:database/order_items/";
    private static final String PATH_ORDERS = "classpath:database/orders/";

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "alice@example.com", roles = ControllerTestUtil.USER)
    @DisplayName("""
            Verify placeOrder() method with correct inputs
            """)
    @Sql(scripts = PATH_CART_ITEMS + "save_two_cartItems_to_cart_items_table.sql",
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = PATH_ORDER_ITEMS + "remove_two_orderItems_from_order_items_table.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = PATH_ORDERS + "remove_alice_order_from_orders_table.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void placeOrder_ValidInputs_ReturnOrderDto() throws Exception {
        // given
        UserDetails userDetails = new UserDetails() {
        }
        User alice = new User().setId(2L);
        when(authentication.getPrincipal()).thenReturn((Object) alice);
        OrderRequestShippingAddressDto shippingAddressDto
                = new OrderRequestShippingAddressDto()
                .setShippingAddress("Shevchenko 123A");
        OrderItemResponseDto smallOrderItemDto
                = new OrderItemResponseDto()
                .setId(1L)
                .setBookId(1L)
                .setQuantity(5);
        OrderItemResponseDto bigOrderItemDto
                = new OrderItemResponseDto()
                .setId(2L)
                .setBookId(2L)
                .setQuantity(8);
        OrderResponseDto orderResponseDto
                = new OrderResponseDto()
                .setId(1L)
                .setStatus("RECEIVED")
                .setTotal(BigDecimal.valueOf(215))
                .setUserId(alice.getId())
                .setOrderItems(Set.of(smallOrderItemDto, bigOrderItemDto));
        String shippingAddressDtoJsonRequest
                = objectMapper.writeValueAsString(shippingAddressDto);

        // when
        MvcResult mvcResult = mockMvc.perform(post(BASE_URL)
                        .content(shippingAddressDtoJsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // then
        OrderResponseDto actual
                = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                OrderResponseDto.class);
        assertNotNull(actual.getOrderDate());
        EqualsBuilder.reflectionEquals(orderResponseDto, actual, "orderDate");
    }

    @Test
    void getAllOrders() {
    }

    @Test
    void updateOrderStatus() {
    }

    @Test
    void getAllOrderItemsByOrderId() {
    }

    @Test
    void getSpecificOrderItem() {
    }
}
