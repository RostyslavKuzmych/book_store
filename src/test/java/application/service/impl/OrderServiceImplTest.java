package application.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import application.dto.item.OrderItemResponseDto;
import application.dto.order.OrderRequestShippingAddressDto;
import application.dto.order.OrderResponseDto;
import application.dto.shopping.cart.ShoppingCartResponseDto;
import application.mapper.OrderItemMapper;
import application.mapper.OrderMapper;
import application.model.Book;
import application.model.CartItem;
import application.model.Order;
import application.model.OrderItem;
import application.model.ShoppingCart;
import application.model.Status;
import application.model.User;
import application.repository.OrderRepository;
import application.service.CartItemService;
import application.service.OrderItemService;
import application.service.ShoppingCartService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    private static final Long ALICE_ID = 1L;
    private static final Integer ONE_TIME = 1;
    private static final Integer TWO_TIMES = 2;
    private static OrderItemResponseDto bigOrderItemDto;
    private static OrderItemResponseDto smallOrderItemDto;
    @Mock
    private OrderItemMapper orderItemMapper;
    @Mock
    private CartItemService cartItemService;
    @Mock
    private OrderItemService orderItemService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private ShoppingCartService shoppingCartService;
    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    @BeforeAll
    static void beforeAll() {
        bigOrderItemDto = new OrderItemResponseDto()
                .setId(1L)
                .setBookId(1L)
                .setQuantity(10);
        smallOrderItemDto = new OrderItemResponseDto()
                .setId(2L)
                .setBookId(2L)
                .setQuantity(5);
    }

    @Test
    @DisplayName("""
            Verify createOrder() method with correct inputs
            """)
    void createOrder_ValidInputs_ReturnOrderDto() {
        // given
        CartItem bigCartItem
                = new CartItem()
                .setId(1L)
                .setBook(new Book().setId(1L).setPrice(BigDecimal.valueOf(10)))
                .setQuantity(10);
        CartItem smallCartItem
                = new CartItem()
                .setId(2L)
                .setBook(new Book().setId(2L).setPrice(BigDecimal.valueOf(12)))
                .setQuantity(5);
        ShoppingCart shoppingCart
                = new ShoppingCart()
                .setId(1L)
                .setUser(new User().setId(2L))
                .setCartItemSet(Set.of(bigCartItem, smallCartItem));
        ShoppingCartResponseDto shoppingCartResponseDto
                = new ShoppingCartResponseDto()
                .setId(shoppingCart.getId())
                .setUserId(shoppingCart.getUser().getId())
                .setCartItems(new HashSet<>());
        OrderRequestShippingAddressDto shippingAddressDto
                = new OrderRequestShippingAddressDto()
                .setShippingAddress("Franko 10");
        Order order = new Order()
                .setId(1L)
                .setShippingAddress(shippingAddressDto.getShippingAddress())
                .setOrderDate(LocalDateTime.now())
                .setUser(shoppingCart.getUser())
                .setStatus(Status.RECEIVED);
        OrderItem bigOrderItem
                = new OrderItem()
                .setId(1L)
                .setBook(bigCartItem.getBook())
                .setQuantity(bigCartItem.getQuantity());
        OrderItem smallOrderItem
                = new OrderItem()
                .setId(2L)
                .setBook(smallCartItem.getBook())
                .setQuantity(smallCartItem.getQuantity());
        OrderResponseDto orderResponseDto
                = new OrderResponseDto()
                .setId(order.getId())
                .setOrderDate(order.getOrderDate())
                .setUserId(order.getUser().getId())
                .setStatus(order.getStatus().toString())
                .setTotal(BigDecimal.valueOf(160))
                .setOrderItems(Set.of(bigOrderItemDto, smallOrderItemDto));

        // when
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderItemMapper.toOrderItem(bigCartItem)).thenReturn(bigOrderItem);
        when(orderItemMapper.toOrderItem(smallCartItem)).thenReturn(smallOrderItem);
        when(orderItemService.save(bigOrderItem)).thenReturn(bigOrderItemDto);
        when(orderItemService.save(smallOrderItem)).thenReturn(smallOrderItemDto);
        when(orderRepository.save(order)).thenReturn(order);
        when(shoppingCartService.clearShoppingCart(shoppingCart))
                .thenReturn(shoppingCartResponseDto);
        doNothing().when(cartItemService).deleteCartItem(any(CartItem.class));
        when(orderMapper.toResponseDto(order)).thenReturn(orderResponseDto);

        // then
        OrderResponseDto actual
                = orderServiceImpl.createOrder(shoppingCart, shippingAddressDto);
        assertNotNull(actual);
        assertEquals(orderResponseDto, actual);
        verify(orderRepository, times(TWO_TIMES)).save(any(Order.class));
    }

    @Test
    @DisplayName("""
            Verify findAllByUserId() method with correct userId
            """)
    void findAllByUserId_ValidUserId_ReturnOneOrder() {
        // given
        Order order = new Order()
                .setId(1L)
                .setStatus(Status.DELIVERED)
                .setUser(new User().setId(ALICE_ID))
                .setOrderDate(LocalDateTime.now().minusDays(2))
                .setShippingAddress("Franko 12")
                .setTotal(BigDecimal.valueOf(160));
        OrderItem bigOrderItem = new OrderItem()
                .setId(1L)
                .setBook(new Book().setId(1L).setPrice(BigDecimal.valueOf(10)))
                .setOrder(order)
                .setQuantity(10)
                .setPrice(BigDecimal.valueOf(100));
        OrderItem smallOrderItem = new OrderItem()
                .setId(2L)
                .setOrder(order)
                .setBook(new Book().setId(2L).setPrice(BigDecimal.valueOf(12)))
                .setQuantity(5)
                .setPrice(BigDecimal.valueOf(60));
        order.setItemSet(Set.of(smallOrderItem, bigOrderItem));
        OrderResponseDto orderResponseDto
                = new OrderResponseDto()
                .setId(order.getId())
                .setOrderDate(order.getOrderDate())
                .setUserId(order.getUser().getId())
                .setStatus(order.getStatus().toString())
                .setTotal(order.getTotal())
                .setOrderItems(Set.of(bigOrderItemDto, smallOrderItemDto));

        // when
        when(orderRepository.findAllByUserId(ALICE_ID))
                .thenReturn(List.of(order));
        when(orderMapper.toResponseDto(order))
                .thenReturn(orderResponseDto);

        // then
        List<OrderResponseDto> expected
                = List.of(orderResponseDto);
        List<OrderResponseDto> actual
                = orderServiceImpl.findAllByUserId(ALICE_ID);
        assertEquals(1, actual.size());
        assertEquals(expected, actual);
        verify(orderRepository, times(ONE_TIME)).findAllByUserId(ALICE_ID);
    }
}
