package application.controller;

import application.dto.cartItem.CartItemRequestDto;
import application.dto.cartItem.CartItemResponseDto;
import application.model.Book;
import application.model.CartItem;
import application.model.ShoppingCart;
import application.model.User;
import application.repository.CartItemRepository;
import application.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Management shopping carts", description = "Endpoints for managing shopping carts")
@RequestMapping("/api/cart")
public class ShoppingCartController {
    private ShoppingCart shoppingCart = new ShoppingCart();
    private final CartItemRepository cartItemRepository;
    private final BookService bookService;

    @PostMapping
    @Operation(summary = "Add a book to the shopping cart",
            description = "An endpoint for adding a book to the shopping cart")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCart addBookToSc(Authentication authentication,
                                    @RequestBody @Valid CartItemRequestDto cartItemRequestDto) {
        IsAddedUser(authentication);
        shoppingCart.setCartItemSet(shoppingCart.getCartItemSet().add(cartItemRepository.save(getCartItem()))); cartItemRepository.save(getCartItem(a))
        shoppingCart.setUser((User) authentication.getPrincipal());
        shoppingCart.s
    }

    private CartItemResponseDto getCartItem(ShoppingCart shoppingCart, CartItemRequestDto requestDto) {
        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(bookService.getBookById(requestDto.getBookId()));
        return cartItemRepository.save(cartItem);
    }

    private void IsAddedUser(Authentication authentication) {
        if (shoppingCart.getUser() == null) {
            shoppingCart.setUser((User) authentication.getPrincipal());
        }
    }

}
