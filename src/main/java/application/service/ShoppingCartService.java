package application.service;

import application.dto.cart.item.CartItemRequestDto;
import application.dto.shopping.cart.ShoppingCartRequestDto;
import application.dto.shopping.cart.ShoppingCartResponseDto;
import application.model.CartItem;
import application.model.ShoppingCart;
import application.model.User;

public interface ShoppingCartService {
    void createShoppingCart(User user);

    void addCartItemToShoppingCart(ShoppingCart shoppingCart, CartItem cartItem);

    void clearShoppingCart(ShoppingCart shoppingCart);

    ShoppingCartResponseDto updateQuantityById(User user,
                                               Long id,
                                               ShoppingCartRequestDto requestDto);

    ShoppingCartResponseDto addBookToShoppingCart(User user,
                                                  CartItemRequestDto cartItemRequestDto);

    ShoppingCartResponseDto getShoppingCartDto(Long userId);
}
