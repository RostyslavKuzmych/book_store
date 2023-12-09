package application.service;

import application.dto.cart.item.CartItemRequestDto;
import application.dto.cart.item.CartItemResponseDto;
import application.model.CartItem;
import application.model.ShoppingCart;

public interface CartItemService {
    CartItemResponseDto findById(Long cartItemId);

    void deleteById(Long cartItemId);

    void deleteCartItem(CartItem cartItem);

    CartItemResponseDto createCartItem(ShoppingCart shoppingCart, CartItemRequestDto requestDto);

    CartItemResponseDto save(CartItem cartItem);
}
