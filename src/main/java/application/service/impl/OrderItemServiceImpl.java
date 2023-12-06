package application.service.impl;

import application.dto.item.OrderItemResponseDto;
import application.exception.InvalidRequestException;
import application.mapper.OrderItemMapper;
import application.model.Order;
import application.model.OrderItem;
import application.repository.OrderItemRepository;
import application.service.OrderItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private static final String INVALID_ORDER_ITEM_ID = "Invalid orderItem id ";
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderItemResponseDto> getAllOrderItemsDtosByOrderId(Long orderId) {
        return orderItemRepository.findAllByOrderId(orderId)
                .stream()
                .map(orderItemMapper::toOrderItemResponseDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto getOrderItemResponseById(Order order, Long id) {
        OrderItem orderItem = order.getItemSet()
                .stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException(INVALID_ORDER_ITEM_ID + id));
        return orderItemMapper.toOrderItemResponseDto(orderItem);
    }

    @Override
    public OrderItemResponseDto save(OrderItem orderItem) {
        return orderItemMapper.toOrderItemResponseDto(orderItemRepository.save(orderItem));
    }
}
