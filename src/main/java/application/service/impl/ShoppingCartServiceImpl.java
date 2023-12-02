package application.service.impl;

import application.dto.shopping.cart.ShoppingCartResponseDto;
import application.mapper.CartItemMapper;
import application.mapper.ShoppingCartMapper;
import application.model.CartItem;
import application.model.ShoppingCart;
import application.model.User;
import application.repository.ShoppingCartRepository;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import application.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCart save(ShoppingCart shoppingCart) {
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart findByUserId(Long id) {
        return shoppingCartRepository.findShoppingCartByUserId(id);
    }

    @Override
    public Set<CartItem> updateSetCartItem(Long id, ShoppingCart s, CartItem c) {
        Set<CartItem> cartItemSet = new HashSet<>();
        for (CartItem cartItem : s.getCartItemSet()) {
            if (cartItem.getId() == id) {
                cartItemSet.add(c);
            } else {
                cartItemSet.add(cartItem);
            }
        }
        return cartItemSet;
    }

    @Override
    public ShoppingCartResponseDto getShoppingCartDto(ShoppingCart shoppingCart, User user) {
        ShoppingCartResponseDto responseDto = shoppingCartMapper.toResponseDto(shoppingCart);
        responseDto.setCartItems(shoppingCart.getCartItemSet()
                .stream()
                .map(cartItemMapper::toCartItemResponseDto)
                .collect(Collectors.toSet()));
        return responseDto;
    }

    @Override
    public void addCartItemToShoppingCart(ShoppingCart shoppingCart, CartItem cartItem) {
        if (shoppingCart.getCartItemSet() == null) {
            shoppingCart.setCartItemSet(new HashSet<>());
        }
        shoppingCart.getCartItemSet().add(cartItem);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void clearShoppingCart(ShoppingCart shoppingCart) {
        shoppingCart.setCartItemSet(new HashSet<>());
    }
}
