package application.controller;

import application.dto.cart.item.CartItemRequestDto;
import application.dto.shopping.cart.ShoppingCartRequestDto;
import application.dto.shopping.cart.ShoppingCartResponseDto;
import application.model.CartItem;
import application.model.ShoppingCart;
import application.model.User;
import application.repository.CartItemRepository;
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
@Tag(name = "Management shopping carts", description = "Endpoints for managing shopping carts")
@RequestMapping("/api/cart")
public class ShoppingCartController {
    private final CartItemRepository cartItemRepository;
    private final CartItemService cartItemService;
    private final ShoppingCartService shoppingCartService;

    @PostMapping
    @Operation(summary = "Add a book to the shopping cart",
            description = "An endpoint for adding a book to the shopping cart")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCartResponseDto addBookToShoppingCart(Authentication authentication,
                                    @RequestBody @Valid CartItemRequestDto cartItemRequestDto) {
        User user = (User) authentication.getPrincipal();
        ShoppingCart shoppingCart = shoppingCartService.findByUserId(user.getId());
        CartItem cartItem = cartItemService.createCartItem(shoppingCart, cartItemRequestDto);
        shoppingCartService.addCartItemToShoppingCart(shoppingCart, cartItem);
        return shoppingCartService.addBookToShoppingCart(authentication, cartItemRequestDto);
    }

    @GetMapping
    @Operation(summary = "Get the shoppingCart",
            description = "An endpoint for getting user's shoppingCart")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartResponseDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        ShoppingCart shoppingCart = shoppingCartService.findByUserId(user.getId());
        return shoppingCartService.getShoppingCartDto(shoppingCart);
    }

    @PutMapping("/cart-items/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update quantity of the book",
            description = "An endpoint for updating quantity of the book")
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartResponseDto updateQuantityById(Authentication authentication,
                                                       @PathVariable Long id,
                                                       @RequestBody @Valid
                                                          ShoppingCartRequestDto requestDto) {
        return shoppingCartService.updateQuantityById(authentication, id, requestDto);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a cartItem",
            description = "An endpoint for deleting a cartItem")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        cartItemService.delete(id);
    }
}
