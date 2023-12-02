package application.service;

import application.dto.order.OrderRequestShippingAddressDto;
import application.dto.order.OrderRequestStatusDto;
import application.dto.order.OrderResponseDto;
import application.model.Order;
import application.model.ShoppingCart;
import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(ShoppingCart shoppingCart,
                                 OrderRequestShippingAddressDto dto);

    List<OrderResponseDto> getListOrderResponseDtos(Long id);

    void updateStatusOrder(Long id, OrderRequestStatusDto dto);

    List<Order> findAllByUserId(Long id);

    Order findByOrderId(Long id);
}
