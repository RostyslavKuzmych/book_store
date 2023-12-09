package application.service.impl;

import application.dto.cart.item.CartItemRequestDto;
import application.dto.cart.item.CartItemResponseDto;
import application.dto.shopping.cart.ShoppingCartRequestDto;
import application.dto.shopping.cart.ShoppingCartResponseDto;
import application.exception.EntityNotFoundException;
import application.mapper.ShoppingCartMapper;
import application.model.CartItem;
import application.model.ShoppingCart;
import application.model.User;
import application.repository.CartItemRepository;
import application.repository.ShoppingCartRepository;
import application.service.CartItemService;
import application.service.ShoppingCartService;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private static final String FIND_CART_ITEM_EXCEPTION = "Can't find cartItem by id ";
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemService cartItemService;
    private final CartItemRepository cartItemRepository;

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
        CartItemResponseDto responseDto
                = cartItemService.createCartItem(shoppingCart, cartItemRequestDto);
        CartItem cartItem = findById(responseDto.getId());
        addCartItemToShoppingCart(shoppingCart, cartItem);
        return shoppingCartMapper.toResponseDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto getShoppingCartDto(Long userId) {
        return shoppingCartMapper
                .toResponseDto(shoppingCartRepository.findShoppingCartByUserId(userId));
    }

    @Override
    public void clearShoppingCart(ShoppingCart shoppingCart) {
        shoppingCart.setCartItemSet(new HashSet<>());
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartResponseDto updateQuantityById(User user,
                                                      Long cartItemId,
                                                      ShoppingCartRequestDto requestDto) {
        CartItem cartItem = findById(cartItemId);
        cartItem.setQuantity(requestDto.getQuantity());
        cartItemService.save(cartItem);
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserId(user.getId());
        shoppingCart.setCartItemSet(updateCartItems(cartItemId, shoppingCart, cartItem));
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toResponseDto(shoppingCart);
    }

    private Set<CartItem> updateCartItems(Long cartItemId, ShoppingCart shoppingCart,
                                          CartItem cartItem) {
        Set<CartItem> cartItemSet = new HashSet<>();
        for (CartItem c : shoppingCart.getCartItemSet()) {
            if (c.getId().equals(cartItemId)) {
                cartItemSet.add(cartItem);
            } else {
                cartItemSet.add(c);
            }
        }
        return cartItemSet;
    }

    private void addCartItemToShoppingCart(ShoppingCart shoppingCart, CartItem cartItem) {
        shoppingCart.getCartItemSet().add(cartItem);
        shoppingCartRepository.save(shoppingCart);
    }

    private CartItem findById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException(FIND_CART_ITEM_EXCEPTION + cartItemId));
    }
}
