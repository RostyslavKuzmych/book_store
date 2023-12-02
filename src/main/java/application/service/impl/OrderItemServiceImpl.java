package application.service.impl;

import application.dto.item.OrderItemResponseDto;
import application.exception.EntityNotFoundException;
import application.exception.InvalidRequestException;
import application.mapper.OrderItemMapper;
import application.model.Order;
import application.model.OrderItem;
import application.model.User;
import application.repository.OrderItemRepository;
import application.repository.OrderRepository;
import application.service.OrderItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private static final String FIND_ORDER_EXCEPTION = "Can't find an order by id ";
    private static final String INVALID_ORDER_ITEM_ID = "Invalid orderItem id ";
    private static final String INVALID_ORDER_ID = "Invalid order id ";
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderRepository orderRepository;

    @Override
    public OrderItem save(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public List<OrderItemResponseDto> getAllOrderItemsDtoByOrderId(Authentication authentication,
                                                                   Long orderId) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(()
                        -> new EntityNotFoundException(FIND_ORDER_EXCEPTION + orderId));
        checkBelongingOrderToUser(order, authentication);
        return orderItemRepository.findAllByOrderId(orderId)
                .stream()
                .map(orderItemMapper::toOrderItemResponseDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto getOrderItemResponseById(Authentication authentication,
                                                         Long orderId,
                                                         Long id) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(()
                        -> new EntityNotFoundException(FIND_ORDER_EXCEPTION + orderId));
        checkBelongingOrderToUser(order, authentication);
        OrderItem orderItem = order.getItemSet()
                .stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException(INVALID_ORDER_ITEM_ID + id));
        return orderItemMapper.toOrderItemResponseDto(orderItem);
    }

    private void checkBelongingOrderToUser(Order order, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (order.getUser().getId() != user.getId()) {
            throw new InvalidRequestException(INVALID_ORDER_ID + order.getId());
        }
    }
}
