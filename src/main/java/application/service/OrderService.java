package application.service;

import application.dto.order.OrderRequestShippingAddressDto;
import application.dto.order.OrderRequestStatusDto;
import application.dto.order.OrderResponseDto;
import application.model.ShoppingCart;
import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(ShoppingCart shoppingCart,
                                 OrderRequestShippingAddressDto dto);

    void updateOrderStatus(Long orderId, OrderRequestStatusDto dto);

    List<OrderResponseDto> findAllByUserId(Long userId);
}
