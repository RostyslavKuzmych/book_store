package application.service.impl;

import application.dto.cart.item.CartItemRequestDto;
import application.dto.cart.item.CartItemResponseDto;
import application.exception.EntityNotFoundException;
import application.mapper.BookMapper;
import application.mapper.CartItemMapper;
import application.model.CartItem;
import application.model.ShoppingCart;
import application.repository.CartItemRepository;
import application.service.BookService;
import application.service.CartItemService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private static final String FIND_EXCEPTION = "Can't find cartItem by id ";
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookService bookService;
    private final BookMapper bookMapper;

    @Override
    public CartItemResponseDto findById(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(()
                -> new EntityNotFoundException(FIND_EXCEPTION + cartItemId));
        return cartItemMapper.toCartItemResponseDto(cartItem);
    }

    @Override
    public void deleteById(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public void deleteCartItem(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
    }

    @Override
    @Transactional
    public CartItemResponseDto createCartItem(ShoppingCart shoppingCart,
                                              CartItemRequestDto requestDto) {
        CartItem cartItem = cartItemMapper.toCartItem(requestDto);
        cartItem.setBook(bookMapper
                .toModelFromDto(bookService.getBookDtoById(cartItem.getBook().getId())));
        cartItem.setShoppingCart(shoppingCart);
        return cartItemMapper.toCartItemResponseDto(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemResponseDto save(CartItem cartItem) {
        return cartItemMapper.toCartItemResponseDto(cartItemRepository.save(cartItem));
    }
}
