package application.service;

import application.dto.item.OrderItemResponseDto;
import application.model.Order;
import application.model.OrderItem;
import java.util.List;

public interface OrderItemService {

    List<OrderItemResponseDto> getAllOrderItemsDtoByOrderId(Long orderId);

    OrderItemResponseDto getOrderItemResponseById(Order order, Long id);

    OrderItemResponseDto save(OrderItem orderItem);
}
