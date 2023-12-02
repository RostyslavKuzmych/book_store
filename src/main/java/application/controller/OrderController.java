package application.controller;

import application.dto.order.OrderRequestShippingAddressDto;
import application.dto.order.OrderResponseDto;
import application.model.ShoppingCart;
import application.model.User;
import application.service.OrderService;
import application.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Management orders", description = "Endpoints for management orders")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final ShoppingCartService shoppingCartService;


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


}
