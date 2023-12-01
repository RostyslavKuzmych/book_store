package application.service;

import application.dto.cart.item.CartItemRequestDto;
import application.model.CartItem;
import application.model.ShoppingCart;

public interface CartItemService {
    CartItem findById(Long id);

    void delete(Long id);

    CartItem createCartItem(ShoppingCart shoppingCart, CartItemRequestDto requestDto);
}
