package application.service.impl;

import application.dto.cart.item.CartItemRequestDto;
import application.dto.shopping.cart.ShoppingCartRequestDto;
import application.dto.shopping.cart.ShoppingCartResponseDto;
import application.mapper.ShoppingCartMapper;
import application.model.CartItem;
import application.model.ShoppingCart;
import application.model.User;
import application.repository.ShoppingCartRepository;
import application.service.CartItemService;
import application.service.ShoppingCartService;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemService cartItemService;

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto addBookToShoppingCart(User user,
                                                         CartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserId(user.getId());
        CartItem cartItem = cartItemService.createCartItem(shoppingCart, cartItemRequestDto);
        addCartItemToShoppingCart(shoppingCart, cartItem);
        return shoppingCartMapper.toResponseDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto getShoppingCartDto(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserId(userId);
        return shoppingCartMapper.toResponseDto(shoppingCart);
    }

    @Override
    public void clearShoppingCart(ShoppingCart shoppingCart) {
        shoppingCart.setCartItemSet(new HashSet<>());
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto updateQuantityById(User user,
                                                      Long id,
                                                      ShoppingCartRequestDto requestDto) {
        CartItem cartItem = cartItemService.findById(id);
        cartItem.setQuantity(requestDto.getQuantity());
        cartItemService.save(cartItem);
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserId(user.getId());
        shoppingCart.setCartItemSet(updateCartItems(id, shoppingCart, cartItem));
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toResponseDto(shoppingCart);
    }

    private Set<CartItem> updateCartItems(Long id, ShoppingCart shoppingCart,
                                          CartItem cartItem) {
        Set<CartItem> cartItemSet = new HashSet<>();
        for (CartItem c : shoppingCart.getCartItemSet()) {
            if (c.getId() == id) {
                cartItemSet.add(cartItem);
            } else {
                cartItemSet.add(c);
            }
        }
        return cartItemSet;
    }

    private void addCartItemToShoppingCart(ShoppingCart shoppingCart, CartItem cartItem) {
        if (shoppingCart.getCartItemSet() == null) {
            shoppingCart.setCartItemSet(new HashSet<>());
        }
        shoppingCart.getCartItemSet().add(cartItem);
        shoppingCartRepository.save(shoppingCart);
    }
}
