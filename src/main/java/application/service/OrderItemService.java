package application.service;

import application.dto.item.OrderItemResponseDto;
import application.model.OrderItem;
import java.util.List;

public interface OrderItemService {

    List<OrderItemResponseDto> getAllOrderItemsDtosByOrderId(Long orderId);

    OrderItemResponseDto getOrderItemResponseById(Long orderId, Long id);

    OrderItemResponseDto save(OrderItem orderItem);
}
