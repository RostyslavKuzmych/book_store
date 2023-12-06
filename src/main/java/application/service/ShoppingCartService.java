package application.service;

import application.dto.cart.item.CartItemRequestDto;
import application.dto.shopping.cart.ShoppingCartRequestDto;
import application.dto.shopping.cart.ShoppingCartResponseDto;
import application.model.CartItem;
import application.model.ShoppingCart;
import application.model.User;
import java.util.Set;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {
    ShoppingCart save(ShoppingCart shoppingCart);

    ShoppingCart findByUserId(Long id);

    ShoppingCart createShoppingCart(User user);

    Set<CartItem> updateSetOfCartItem(Long id, ShoppingCart s, CartItem c);

    void addCartItemToShoppingCart(ShoppingCart shoppingCart, CartItem cartItem);

    void clearShoppingCart(ShoppingCart shoppingCart);

    ShoppingCartResponseDto updateQuantityById(Authentication authentication,
                                               Long id,
                                               ShoppingCartRequestDto requestDto);

    ShoppingCartResponseDto addBookToShoppingCart(Authentication authentication,
                                                         CartItemRequestDto cartItemRequestDto);
}
