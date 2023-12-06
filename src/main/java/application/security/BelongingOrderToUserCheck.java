package application.security;

import application.exception.EntityNotFoundException;
import application.exception.InvalidRequestException;
import application.model.Order;
import application.model.User;
import application.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BelongingOrderToUserCheck {
    private final OrderRepository orderRepository;

    public Order checkBelongingOrderToUser(Authentication authentication, Long id) {
        User user = (User) authentication.getPrincipal();
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order by id " + id));
        if (order.getUser().getId() != user.getId()) {
            throw new InvalidRequestException("This order doesn't belong to you!");
        }
        return order;
    }
}
