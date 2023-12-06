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
    private static final String FIND_EXCEPTION = "Can't find order by id ";
    private static final String BELONGING_EXCEPTION = "This order doesn't belong to you!";
    private final OrderRepository orderRepository;

    public Order checkBelongingOrderToUser(Authentication authentication, Long id) {
        User user = (User) authentication.getPrincipal();
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(FIND_EXCEPTION + id));
        if (order.getUser().getId() != user.getId()) {
            throw new InvalidRequestException(BELONGING_EXCEPTION);
        }
        return order;
    }
}
