package application.service;

import application.dto.item.OrderItemResponseDto;
import application.model.OrderItem;
import java.util.List;

public interface OrderItemService {

    List<OrderItemResponseDto> getAllOrderItemDtosByOrderId(Long orderId);

    OrderItemResponseDto getOrderItemResponseById(Long orderId, Long orderItemId);

    OrderItemResponseDto save(OrderItem orderItem);
}
