package application.controller;

import application.dto.item.OrderItemResponseDto;
import application.dto.order.OrderRequestShippingAddressDto;
import application.dto.order.OrderRequestStatusDto;
import application.dto.order.OrderResponseDto;
import application.model.Order;
import application.model.User;
import application.security.BelongingOrderToUserCheck;
import application.service.OrderItemService;
import application.service.OrderService;
import application.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Management orders", description = "Endpoints for management orders")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final ShoppingCartService shoppingCartService;
    private final OrderItemService orderItemService;
    private final BelongingOrderToUserCheck belongingOrderToUserCheck;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Place an order", description = "An endpoint for placing an order")
    @PreAuthorize("hasRole('USER')")
    public OrderResponseDto placeOrder(Authentication authentication,
                                       @RequestBody @Valid OrderRequestShippingAddressDto dto) {
        User user = (User) authentication.getPrincipal();
        return orderService
                .createOrder(shoppingCartService.findByUserId(user.getId()), dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all orders", description = "An endpoint for getting all orders")
    @PreAuthorize("hasRole('USER')")
    public List<OrderResponseDto> getAllOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAllByUserId(user.getId());
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update order status",
            description = "An endpoint for updating an order status")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateOrderStatus(@PathVariable Long id,
                                  @RequestBody OrderRequestStatusDto dto) {
        orderService.updateOrderStatus(id, dto);
    }

    @GetMapping("/{orderId}/items")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all orderItems by order id",
            description = "An endpoint for getting all orderItems by order id")
    @PreAuthorize("hasRole('USER')")
    public List<OrderItemResponseDto> getAllOrderItemsByOrderId(Authentication authentication,
                                                                @PathVariable Long orderId) {
        belongingOrderToUserCheck.checkBelongingOrderToUser(authentication, orderId);
        return orderItemService.getAllOrderItemsDtoByOrderId(orderId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get a specific orderItem",
            description = "An endpoint for getting a specific orderItem")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    public OrderItemResponseDto getSpecificOrderItem(Authentication authentication,
                                                     @PathVariable Long orderId,
                                                     @PathVariable Long itemId) {
        Order order = belongingOrderToUserCheck.checkBelongingOrderToUser(authentication, orderId);
        return orderItemService
                .getOrderItemResponseById(order, itemId);
    }
}
