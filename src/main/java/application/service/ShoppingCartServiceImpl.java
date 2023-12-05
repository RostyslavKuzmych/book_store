package application.service;

import application.dto.cart.item.CartItemRequestDto;
import application.dto.shopping.cart.ShoppingCartRequestDto;
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

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final CartItemService cartItemService;

    @Override
    public ShoppingCart save(ShoppingCart shoppingCart) {
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart findByUserId(Long id) {
        return shoppingCartRepository.findShoppingCartByUserId(id);
    }

    @Override
    public ShoppingCart createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto addBookToShoppingCart(Authentication authentication,
                                                         CartItemRequestDto cartItemRequestDto) {
        User user = (User) authentication.getPrincipal();
        ShoppingCart shoppingCart = findByUserId(user.getId());
        CartItem cartItem = cartItemService.createCartItem(shoppingCart, cartItemRequestDto);
        addCartItemToShoppingCart(shoppingCart, cartItem);
        return getShoppingCartDto(shoppingCart);
    }

    @Override
    public Set<CartItem> updateSetOfCartItem(Long id, ShoppingCart shoppingCart,
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

    @Override
    public ShoppingCartResponseDto getShoppingCartDto(ShoppingCart shoppingCart) {
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
    @Transactional
    public ShoppingCartResponseDto updateQuantityById(Authentication authentication,
                                                      Long id,
                                                      ShoppingCartRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        CartItem cartItem = cartItemService.findById(id);
        cartItem.setQuantity(requestDto.getQuantity());
        cartItemService.save(cartItem);
        ShoppingCart shoppingCart = findByUserId(user.getId());
        shoppingCart.setCartItemSet(updateSetOfCartItem(id, shoppingCart, cartItem));
        save(shoppingCart);
        return getShoppingCartDto(shoppingCart);
    }
}
