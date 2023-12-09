package application.controller;

import application.dto.cart.item.CartItemRequestDto;
import application.dto.shopping.cart.ShoppingCartRequestDto;
import application.dto.shopping.cart.ShoppingCartResponseDto;
import application.model.User;
import application.service.CartItemService;
import application.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "ShoppingCart management", description = "Endpoints for shoppingCart management")
@RequestMapping("/api/carts")
@PreAuthorize("hasRole('USER')")
public class ShoppingCartController {
    private final CartItemService cartItemService;
    private final ShoppingCartService shoppingCartService;

    @PostMapping
    @Operation(summary = "Add a book to the shopping cart",
            description = "Endpoint for adding a book to the shopping cart")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCartResponseDto addBookToShoppingCart(Authentication authentication,
                                    @RequestBody @Valid CartItemRequestDto cartItemRequestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addBookToShoppingCart(user, cartItemRequestDto);
    }

    @GetMapping
    @Operation(summary = "Get a shoppingCart",
            description = "Endpoint for getting user's shoppingCart from the db")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartResponseDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getShoppingCartDto(user.getId());
    }

    @PutMapping("/cart-items/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update books quantity",
            description = "Endpoint for updating books quantity in the db")
    public ShoppingCartResponseDto updateQuantityById(Authentication authentication,
                                                       @PathVariable Long id,
                                                       @RequestBody @Valid
                                                          ShoppingCartRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateQuantityById(user, id, requestDto);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a cartItem",
            description = "Endpoint for deleting a cartItem from the db")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        cartItemService.deleteById(id);
    }
}
