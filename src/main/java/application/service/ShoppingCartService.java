package application.service;

import application.dto.cart.item.CartItemRequestDto;
import application.dto.shopping.cart.ShoppingCartRequestDto;
import application.dto.shopping.cart.ShoppingCartResponseDto;
import application.model.ShoppingCart;
import application.model.User;

public interface ShoppingCartService {
    void createShoppingCart(User user);

    ShoppingCartResponseDto getShoppingCartDto(Long userId);

    void clearShoppingCart(ShoppingCart shoppingCart);

    ShoppingCartResponseDto updateQuantityById(User user,
                                               Long id,
                                               ShoppingCartRequestDto requestDto);

    ShoppingCartResponseDto addBookToShoppingCart(User user,
                                                  CartItemRequestDto cartItemRequestDto);
}
