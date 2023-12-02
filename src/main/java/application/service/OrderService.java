package application.service;

import application.dto.order.OrderRequestShippingAddressDto;
import application.dto.order.OrderResponseDto;
import application.dto.order_item.OrderItemResponseDto;
import application.model.Order;
import application.model.ShoppingCart;

public interface OrderService {
    OrderResponseDto createOrder(ShoppingCart shoppingCart,
                                 OrderRequestShippingAddressDto dto);
}
