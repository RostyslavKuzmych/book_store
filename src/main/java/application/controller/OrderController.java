package application.controller;

import application.dto.item.OrderItemResponseDto;
import application.dto.order.OrderRequestShippingAddressDto;
import application.dto.order.OrderRequestStatusDto;
import application.dto.order.OrderResponseDto;
import application.model.User;
import application.repository.ShoppingCartRepository;
import application.service.OrderItemService;
import application.service.OrderService;
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

@Tag(name = "Order management", description = "Endpoints for order management")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemService orderItemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Place an order",
            description = "Endpoint for saving an order to the db")
    @PreAuthorize("hasRole('USER')")
    public OrderResponseDto placeOrder(Authentication authentication,
                                       @RequestBody @Valid OrderRequestShippingAddressDto dto) {
        User user = (User) authentication.getPrincipal();
        return orderService
                .createOrder(shoppingCartRepository
                        .findShoppingCartByUserId(user.getId()), dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all orders",
            description = "Endpoint for getting all orders from the db")
    @PreAuthorize("hasRole('USER')")
    public List<OrderResponseDto> getAllOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAllByUserId(user.getId());
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update an order status",
            description = "Endpoint for updating an order status")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateOrderStatus(@PathVariable Long id,
                                  @RequestBody OrderRequestStatusDto dto) {
        orderService.updateOrderStatus(id, dto);
    }

    @GetMapping("/{orderId}/items")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all orderItems by order id",
            description = "Endpoint for getting all orderItems by order id from the db")
    @PreAuthorize("hasRole('USER') and @belongingCheck"
            + ".checkBelongingOrderToUser(authentication, #orderId)")
    public List<OrderItemResponseDto> getAllOrderItemsByOrderId(@PathVariable Long orderId) {
        return orderItemService.getAllOrderItemDtosByOrderId(orderId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get a specific orderItem",
            description = "Endpoint for getting a specific orderItem from the db")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER') and @belongingCheck"
            + ".checkBelongingOrderToUser(authentication, #orderId)")
    public OrderItemResponseDto getSpecificOrderItem(@PathVariable Long orderId,
                                                     @PathVariable Long itemId) {
        return orderItemService
                .getOrderItemResponseById(orderId, itemId);
    }
}
