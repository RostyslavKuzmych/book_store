package application.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import application.dto.item.OrderItemResponseDto;
import application.mapper.OrderItemMapper;
import application.model.Book;
import application.model.Order;
import application.model.OrderItem;
import application.model.Status;
import application.model.User;
import application.repository.OrderItemRepository;
import application.repository.OrderRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceImplTest {
    private static final Integer ONE_TIME = 1;
    private static final String SHIPPING_ADDRESS = "Shevchenko 22";
    private static final String BOB_EMAIL = "bob@example.com";
    private static Order order;
    private static OrderItem bigOrderItem;
    private static OrderItem smallOrderItem;
    private static OrderItemResponseDto bigOrderItemDto;
    private static OrderItemResponseDto smallOrderItemDto;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderItemMapper orderItemMapper;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderItemServiceImpl orderItemServiceImpl;

    @BeforeAll
    static void beforeAll() {
        order = new Order()
                .setId(2L)
                .setShippingAddress(SHIPPING_ADDRESS)
                .setOrderDate(LocalDateTime.now().minusDays(3))
                .setUser(new User().setId(2L).setEmail(BOB_EMAIL))
                .setStatus(Status.DELIVERED);
        bigOrderItem
                = new OrderItem()
                .setId(1L)
                .setOrder(order)
                .setQuantity(5)
                .setBook(new Book().setId(2L).setPrice(BigDecimal.valueOf(20)))
                .setPrice(BigDecimal.valueOf(100));
        smallOrderItem
                = new OrderItem()
                .setId(2L)
                .setOrder(order)
                .setQuantity(2)
                .setBook(new Book().setId(3L).setPrice(BigDecimal.valueOf(15)))
                .setPrice(BigDecimal.valueOf(30));
        order.setItemSet(Set.of(bigOrderItem, smallOrderItem));
        bigOrderItemDto = new OrderItemResponseDto()
                .setId(bigOrderItem.getId())
                .setBookId(bigOrderItem.getBook().getId())
                .setQuantity(bigOrderItem.getQuantity());
        smallOrderItemDto = new OrderItemResponseDto()
                .setId(smallOrderItem.getId())
                .setBookId(smallOrderItem.getBook().getId())
                .setQuantity(smallOrderItem.getQuantity());
    }

    @Test
    @DisplayName("""
            Verify getAllOrderItemDtosByOrderId() method with correct orderId
            """)
    void getAllOrderItemDtosByOrderId_ValidOrderId_ReturnTwoOrderItemDtos() {
        // when
        when(orderItemRepository.findAllByOrderId(order.getId()))
                .thenReturn(List.of(bigOrderItem, smallOrderItem));
        when(orderItemMapper.toOrderItemResponseDto(bigOrderItem))
                .thenReturn(bigOrderItemDto);
        when(orderItemMapper.toOrderItemResponseDto(smallOrderItem))
                .thenReturn(smallOrderItemDto);

        // then
        List<OrderItemResponseDto> expected = List.of(bigOrderItemDto, smallOrderItemDto);
        List<OrderItemResponseDto> actual
                = orderItemServiceImpl.getAllOrderItemDtosByOrderId(order.getId());
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
        verify(orderItemRepository, times(ONE_TIME)).findAllByOrderId(order.getId());
    }

    @Test
    @DisplayName("""
            Verify getOrderItemResponseById() method with correct orderItemId
            """)
    void getOrderItemResponseById_ValidOrderItemId_ReturnOrderItemDto() {
        // when
        when(orderRepository.findById(order.getId()))
                .thenReturn(Optional.of(order));
        when(orderItemMapper.toOrderItemResponseDto(smallOrderItem))
                .thenReturn(smallOrderItemDto);

        // then
        OrderItemResponseDto actual
                = orderItemServiceImpl
                .getOrderItemResponseById(order.getId(), smallOrderItem.getId());
        assertNotNull(actual);
        assertEquals(smallOrderItemDto, actual);
    }

    @Test
    @DisplayName("""
            Verify save() method with orderItem
            """)
    void save_ValidOrderItem_ReturnOrderItemDto() {
        // when
        when(orderItemRepository.save(bigOrderItem)).thenReturn(bigOrderItem);
        when(orderItemMapper.toOrderItemResponseDto(bigOrderItem))
                .thenReturn(bigOrderItemDto);

        // then
        OrderItemResponseDto actual
                = orderItemServiceImpl.save(bigOrderItem);
        assertNotNull(actual);
        assertEquals(bigOrderItemDto, actual);
        verify(orderItemRepository, times(ONE_TIME)).save(bigOrderItem);
    }
}
