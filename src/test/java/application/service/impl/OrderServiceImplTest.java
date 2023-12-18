package application.service.impl;

import application.dto.cart.item.CartItemResponseDto;
import application.dto.item.OrderItemResponseDto;
import application.dto.order.OrderRequestShippingAddressDto;
import application.dto.order.OrderResponseDto;
import application.dto.shopping.cart.ShoppingCartResponseDto;
import application.mapper.OrderItemMapper;
import application.mapper.OrderMapper;
import application.mapper.ShoppingCartMapper;
import application.model.*;
import application.repository.CartItemRepository;
import application.repository.OrderRepository;
import application.repository.ShoppingCartRepository;
import application.service.CartItemService;
import application.service.OrderItemService;
import application.service.ShoppingCartService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    private static final Long ALICE_ID = 1L;
    private static final Integer ONE_TIME = 1;
    private static final Integer TWO_TIMES = 2;
    @Mock
    private OrderItemMapper orderItemMapper;
    @Mock
    private ShoppingCartService shoppingCartService;
    @Mock
    private CartItemService cartItemService;
    @Mock
    private OrderItemService orderItemService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    @Test
    @DisplayName("""
            Verify createOrder() method with correct inputs
            """)
    void createOrder_ValidInputs_ReturnOrderDto() {
        CartItem bigCartItem
                = new CartItem()
                .setId(1L)
                .setBook(new Book().setId(2L).setPrice(BigDecimal.valueOf(10)))
                .setQuantity(10);
        CartItem smallCartItem
                = new CartItem()
                .setId(2L)
                .setBook(new Book().setId(1L).setPrice(BigDecimal.valueOf(12)))
                .setQuantity(5);
        ShoppingCart shoppingCart
                = new ShoppingCart()
                .setId(1L)
                .setUser(new User().setId(2L))
                .setCartItemSet(Set.of(bigCartItem, smallCartItem));
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
                .setQuantity(bigCartItem.getQuantity())
                .setBook(bigCartItem.getBook());
        OrderItem smallOrderItem
                = new OrderItem()
                .setId(2L)
                .setBook(smallCartItem.getBook())
                .setQuantity(smallCartItem.getQuantity());
        OrderItemResponseDto bigOrderItemDto
                = new OrderItemResponseDto()
                .setId(bigCartItem.getId())
                .setBookId(bigOrderItem.getBook().getId())
                .setQuantity(bigCartItem.getQuantity());
        OrderItemResponseDto smallOrderItemDto
                = new OrderItemResponseDto()
                .setId(smallOrderItem.getId())
                .setBookId(smallOrderItem.getBook().getId())
                .setQuantity(smallOrderItem.getQuantity());
        OrderResponseDto orderResponseDto
                = new OrderResponseDto()
                .setId(order.getId())
                .setOrderDate(order.getOrderDate())
                .setUserId(order.getUser().getId())
                .setStatus(order.getStatus().toString())
                .setTotal(BigDecimal.valueOf(160))
                .setOrderItems(Set.of(bigOrderItemDto, smallOrderItemDto));

        when(orderRepository.save(any(Order.class))).thenReturn(order);
        Mockito.lenient().
                when(orderItemMapper.toOrderItem(bigCartItem)).thenReturn(bigOrderItem);
        Mockito.lenient().
                when(orderItemMapper.toOrderItem(smallCartItem)).thenReturn(smallOrderItem);
        bigOrderItem.setOrder(order);
        bigOrderItem.setPrice(BigDecimal.valueOf(100));
        smallOrderItem.setOrder(order);
        smallOrderItem.setPrice(BigDecimal.valueOf(60));
        Mockito.lenient()
                .when(orderItemService.save(bigOrderItem)).thenReturn(bigOrderItemDto);
        Mockito.lenient()
                .when(orderItemService.save(smallOrderItem)).thenReturn(smallOrderItemDto);
        order.setItemSet(Set.of(bigOrderItem, smallOrderItem));
        order.setTotal(BigDecimal.valueOf(160));
        when(orderRepository.save(order)).thenReturn(order);
        shoppingCart.setCartItemSet(new HashSet<>());
        Mockito.lenient().
                when(shoppingCartRepository.save(shoppingCart)).thenReturn(shoppingCart);
        Mockito.lenient().when(shoppingCartMapper.toResponseDto(shoppingCart))
                .thenReturn(new ShoppingCartResponseDto()
                        .setId(shoppingCart.getId())
                        .setUserId(shoppingCart.getUser().getId())
                        .setCartItems(new HashSet<>()));
        Mockito.lenient()
                .doNothing().when(cartItemService).deleteCartItem(any(CartItem.class));
        when(orderMapper.toResponseDto(order)).thenReturn(orderResponseDto);

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
    void findAllByUserId_ValidUserId_ReturnTwoOrder() {
        Order order = new Order()
                .setId(1L)
                .setStatus(Status.DELIVERED)
                .setUser(new User().setId(ALICE_ID))
                .setOrderDate(LocalDateTime.now().minusDays(2))
                .setShippingAddress("Franko 12")
                .setTotal(BigDecimal.valueOf(120));
        OrderItem smallOrderItem = new OrderItem()
                .setId(1L)
                .setOrder(order)
                .setBook(new Book().setId(2L).setPrice(BigDecimal.valueOf(10)))
                .setQuantity(3)
                .setPrice(BigDecimal.valueOf(30));
        OrderItem bigOrderItem = new OrderItem()
                .setId(2L)
                .setBook(new Book().setId(1L).setPrice(BigDecimal.valueOf(12)))
                .setOrder(order)
                .setQuantity(5)
                .setPrice(BigDecimal.valueOf(60));
        order.setItemSet(Set.of(smallOrderItem, bigOrderItem));
        OrderItemResponseDto smallOrderItemDto
                = new OrderItemResponseDto()
                .setId(smallOrderItem.getId())
                .setBookId(smallOrderItem.getBook().getId())
                .setQuantity(smallOrderItem.getQuantity());
        OrderItemResponseDto bigOrderItemDto
                = new OrderItemResponseDto()
                .setId(bigOrderItem.getId())
                .setBookId(bigOrderItem.getBook().getId())
                .setQuantity(bigOrderItem.getQuantity());
        OrderResponseDto orderResponseDto
                = new OrderResponseDto()
                .setId(order.getId())
                .setOrderDate(order.getOrderDate())
                .setUserId(order.getUser().getId())
                .setStatus(order.getStatus().toString())
                .setTotal(order.getTotal())
                .setOrderItems(Set.of(bigOrderItemDto, smallOrderItemDto));

        when(orderRepository.findAllByUserId(ALICE_ID))
                .thenReturn(List.of(order));
        when(orderMapper.toResponseDto(order))
                .thenReturn(orderResponseDto);

        List<OrderResponseDto> expected
                = List.of(orderResponseDto);
        List<OrderResponseDto> actual
                = orderServiceImpl.findAllByUserId(ALICE_ID);
        assertEquals(1, actual.size());
        assertEquals(expected, actual);
        verify(orderRepository, times(ONE_TIME)).findAllByUserId(ALICE_ID);
    }
}
