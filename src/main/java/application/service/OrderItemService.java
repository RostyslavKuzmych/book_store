package application.service;

import application.dto.item.OrderItemResponseDto;
import application.model.OrderItem;
import java.util.List;
import org.springframework.security.core.Authentication;

public interface OrderItemService {
    OrderItem save(OrderItem orderItem);

    List<OrderItemResponseDto> getAllOrderItemsDtoByOrderId(Authentication authentication,
                                                            Long orderId);

    OrderItemResponseDto getOrderItemResponseById(Authentication a, Long orderId, Long id);
}
