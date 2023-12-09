package application.security;

import application.exception.EntityNotFoundException;
import application.model.Order;
import application.model.User;
import application.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("belongingCheck")
@RequiredArgsConstructor
public class BelongingOrderToUserCheck {
    private static final String FIND_EXCEPTION = "Can't find order by id ";
    private final OrderRepository orderRepository;

    public boolean checkBelongingOrderToUser(Authentication authentication, Long orderId) {
        User user = (User) authentication.getPrincipal();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(FIND_EXCEPTION + orderId));
        return order.getUser().getId().equals(user.getId());
    }
}
