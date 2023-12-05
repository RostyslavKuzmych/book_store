package application.service.impl;

import application.dto.order.OrderRequestShippingAddressDto;
import application.dto.order.OrderRequestStatusDto;
import application.dto.order.OrderResponseDto;
import application.exception.EntityNotFoundException;
import application.mapper.OrderItemMapper;
import application.mapper.OrderMapper;
import application.model.CartItem;
import application.model.Order;
import application.model.OrderItem;
import application.model.ShoppingCart;
import application.repository.OrderRepository;
import application.service.CartItemService;
import application.service.OrderItemService;
import application.service.OrderService;
import application.service.ShoppingCartService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private static final String FIND_ORDER_EXCEPTION = "Can't find order by id ";
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartService shoppingCartService;
    private final CartItemService cartItemService;
    private final OrderItemService orderItemService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponseDto createOrder(ShoppingCart shoppingCart,
                                        OrderRequestShippingAddressDto dto) {
        Order order = prepareOrder(shoppingCart, dto);
        orderRepository.save(order);
        Set<CartItem> cartItemSet = shoppingCart.getCartItemSet();
        shoppingCartService.clearShoppingCart(shoppingCart);
        deleteAllCartItems(cartItemSet);
        OrderResponseDto responseDto = orderMapper.toResponseDto(order);
        return setOrderItemsDtos(responseDto, order);
    }

    @Override
    public List<OrderResponseDto> getListOrderResponseDtos(Long id) {
        return findAllByUserId(id)
                .stream()
                .map(orderMapper::toResponseDto)
                .map(c -> setOrderItemsDtos(c, findByOrderId(c.getId())
                )).toList();

    }

    @Override
    public void updateOrderStatus(Long id, OrderRequestStatusDto dto) {
        Order order = findByOrderId(id);
        order.setStatus(dto.getStatus());
        orderRepository.save(order);
    }

    @Override
    public List<Order> findAllByUserId(Long id) {
        return orderRepository.findAllByUserId(id);
    }

    @Override
    public Order findByOrderId(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(FIND_ORDER_EXCEPTION + id));
    }

    private Order prepareOrder(ShoppingCart sc, OrderRequestShippingAddressDto dto) {
        Order order = new Order();
        order.setShippingAddress(dto.getShippingAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setUser(sc.getUser());
        order.setStatus(Order.Status.RECEIVED);
        Order savedOrder = orderRepository.save(order);
        order.setItemSet(setOrderItemsToOrderFromShoppingCart(savedOrder, sc));
        order.setTotal(getTotalPrice(order.getItemSet()));
        return order;
    }

    private OrderItem addPriceToOrderItem(OrderItem orderItem, Order order) {
        orderItem.setOrder(order);
        BigDecimal bigDecimal = orderItem.getBook().getPrice();
        orderItem.setPrice(bigDecimal.multiply(new BigDecimal(orderItem.getQuantity())));
        return orderItem;
    }

    private OrderResponseDto setOrderItemsDtos(OrderResponseDto dto, Order order) {
        dto.setOrderItems(order.getItemSet()
                .stream()
                .map(orderItemMapper::toOrderItemResponseDto)
                .collect(Collectors.toSet()));
        return dto;
    }

    private BigDecimal getTotalPrice(Set<OrderItem> orderItems) {
        BigDecimal bigDecimal = orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return bigDecimal;
    }

    private Set<OrderItem> setOrderItemsToOrderFromShoppingCart(Order order,
                                                             ShoppingCart shoppingCart) {
        Set<OrderItem> items = shoppingCart.getCartItemSet()
                .stream()
                .map(orderItemMapper::toOrderItem)
                .map(o -> addPriceToOrderItem(o, order))
                .collect(Collectors.toSet());
        items.stream().forEach(orderItemService::save);
        return items;
    }

    public void deleteAllCartItems(Set<CartItem> cartItemSet) {
        cartItemSet.stream().forEach(cartItemService::deleteCartItem);
    }
}
