package application.controller;

import application.dto.cart.item.CartItemRequestDto;
import application.dto.shopping.cart.ShoppingCartRequestDto;
import application.dto.shopping.cart.ShoppingCartResponseDto;
import application.mapper.CartItemMapper;
import application.mapper.ShoppingCartMapper;
import application.model.CartItem;
import application.model.ShoppingCart;
import application.model.User;
import application.repository.CartItemRepository;
import application.service.CartItemService;
import application.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
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
    private final CartItemMapper cartItemMapper;
    private final CartItemService cartItemService;
    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartMapper shoppingCartMapper;

    @PostMapping
    @Operation(summary = "Add a book to the shopping cart",
            description = "An endpoint for adding a book to the shopping cart")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCartResponseDto addBookToSc(Authentication authentication,
                                    @RequestBody @Valid CartItemRequestDto cartItemRequestDto) {
        User user = (User) authentication.getPrincipal();
        ShoppingCart shoppingCart = shoppingCartService.findByUserId(user.getId());
        addCartItemToShoppingCart(shoppingCart, getCartItem(shoppingCart, cartItemRequestDto));
        return getShoppingCartDto(user);
    }

    @GetMapping
    @Operation(summary = "Get the shoppingCart",
            description = "An endpoint for getting user's shoppingCart")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartResponseDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return getShoppingCartDto(user);
    }

    @PutMapping("/cart-items/{id}")
    @ResponseStatus(HttpStatus.CONTINUE)
    @Operation(summary = "Update quantity of the book",
            description = "An endpoint for updating quantity of the book")
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartResponseDto updateQuantityById(Authentication authentication,
                                                      @PathVariable Long id,
                                                      @Valid @RequestBody
                                                          ShoppingCartRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        CartItem cartItem = cartItemService.findById(id);
        cartItem.setQuantity(requestDto.getQuantity());
        cartItemRepository.save(cartItem);
        ShoppingCart shoppingCart = shoppingCartService.findByUserId(user.getId());
        Set<CartItem> collect = getSetCartItem(id, shoppingCart, cartItem);
        shoppingCart.setCartItemSet(collect);
        shoppingCartService.save(shoppingCart);
        return getShoppingCartDto(user);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a cartItem",
            description = "An endpoint for deleting a cartItem")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        cartItemService.delete(id);
    }

    private ShoppingCartResponseDto getShoppingCartDto(User user) {
        ShoppingCart shoppingCart = shoppingCartService.findByUserId(user.getId());
        ShoppingCartResponseDto responseDto = shoppingCartMapper.toResponseDto(shoppingCart);
        responseDto.setCartItems(shoppingCart.getCartItemSet()
                .stream()
                .map(cartItemMapper::toCartItemResponseDto)
                .collect(Collectors.toSet()));
        return responseDto;
    }

    private Set<CartItem> getSetCartItem(Long id, ShoppingCart s, CartItem c) {
        Set<CartItem> cartItemSet = new HashSet<>();
        for (CartItem cartItem : s.getCartItemSet()) {
            if (cartItem.getId() == id) {
                cartItemSet.add(c);
            }
            cartItemSet.add(cartItem);
        }
        return cartItemSet;
    }

    private CartItem getCartItem(ShoppingCart shoppingCart, CartItemRequestDto requestDto) {
        CartItem cartItem = cartItemMapper.toCartItem(requestDto);
        cartItem.setShoppingCart(shoppingCart);
        return cartItemRepository.save(cartItem);
    }

    private void addCartItemToShoppingCart(ShoppingCart shoppingCart, CartItem cartItem) {
        Set<CartItem> cartItemSet = shoppingCart.getCartItemSet();
        if (cartItemSet == null) {
            shoppingCart.setCartItemSet(new HashSet<>((Collection) cartItem));
            return;
        }
        cartItemSet.add(cartItem);
        shoppingCart.setCartItemSet(cartItemSet);
        shoppingCartService.save(shoppingCart);
    }
}
