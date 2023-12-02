package application.service;

import application.dto.shopping.cart.ShoppingCartResponseDto;
import application.model.CartItem;
import application.model.ShoppingCart;
import application.model.User;
import java.util.Set;

public interface ShoppingCartService {
    ShoppingCart save(ShoppingCart shoppingCart);

    ShoppingCart findByUserId(Long id);

    Set<CartItem> updateSetCartItem(Long id, ShoppingCart s, CartItem c);

    ShoppingCartResponseDto getShoppingCartDto(ShoppingCart shoppingCart, User user);

    void addCartItemToShoppingCart(ShoppingCart shoppingCart, CartItem cartItem);

    void clearShoppingCart(ShoppingCart shoppingCart);
}
