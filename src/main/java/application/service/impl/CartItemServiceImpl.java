package application.service.impl;

import application.dto.cart.item.CartItemRequestDto;
import application.exception.EntityNotFoundException;
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
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookService bookService;

    @Override
    public CartItem findById(Long id) {
        return cartItemRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Can't find cartItem by id " + id));
    }

    @Override
    public void delete(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public void deleteCartItem(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
    }

    @Override
    @Transactional
    public CartItem createCartItem(ShoppingCart shoppingCart, CartItemRequestDto requestDto) {
        CartItem cartItem = cartItemMapper.toCartItem(requestDto);
        cartItem.setBook(bookService.getBookById(cartItem.getBook().getId()));
        cartItem.setShoppingCart(shoppingCart);
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem save(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }
}
