package application.service.impl;

import application.dto.order.OrderRequestShippingAddressDto;
import application.dto.order.OrderResponseDto;
import application.mapper.OrderItemMapper;
import application.mapper.OrderMapper;
import application.model.CartItem;
import application.model.Order;
import application.model.OrderItem;
import application.model.ShoppingCart;
import application.repository.OrderItemRepository;
import application.repository.OrderRepository;
import application.service.CartItemService;
import application.service.OrderItemService;
import application.service.OrderService;
import application.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartService shoppingCartService;
    private final CartItemService cartItemService;
    private final OrderItemService orderItemService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDto createOrder(ShoppingCart shoppingCart, OrderRequestShippingAddressDto dto) {
        Order order = Order.builder().shippingAddress(dto.getShippingAddress())
                .localDateTime(LocalDateTime.now())
                .user(shoppingCart.getUser())
                .status(Order.Status.RECEIVED).build();
        order.setItemSet(saveSetOrderItemsFromShoppingCart(order, shoppingCart));
        System.out.println(order);
        order.setTotal(getTotalPrice(order.getItemSet()));
        System.out.println(order);
        orderRepository.save(order);
        Set<CartItem> cartItemSet = shoppingCart.getCartItemSet();
        shoppingCartService.clearShoppingCart(shoppingCart);
        deleteAllCartItems(cartItemSet);
        OrderResponseDto responseDto = orderMapper.toResponseDto(order);
        setOrderItemsDtos(responseDto, order);
        return responseDto;
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
        System.out.println(bigDecimal);
        return bigDecimal;
    }

    private Set<OrderItem> saveSetOrderItemsFromShoppingCart(Order order,
                                                             ShoppingCart shoppingCart) {
        Set<OrderItem> items = shoppingCart.getCartItemSet()
                .stream()
                .map(orderItemMapper::toOrderItem)
                .map(o -> addPriceToOrderItem(o, order))
                .collect(Collectors.toSet());
        items.stream().forEach(orderItemService::save);
        return items;
    }

    private void deleteAllCartItems(Set<CartItem> cartItemSet) {
        cartItemSet.stream().forEach(cartItemService::deleteCartItem);
    }
}
