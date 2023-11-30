package application.service;

import application.exception.EntityNotFoundException;
import application.model.CartItem;
import application.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem findById(Long id) {
        return cartItemRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Can't find cartItem by id " + id));
    }

    @Override
    public void delete(Long id) {
        cartItemRepository.deleteById(id);
    }
}
