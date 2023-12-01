package application.service;

import application.model.ShoppingCart;
import application.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public ShoppingCart save(ShoppingCart shoppingCart) {
        return shoppingCartRepository(shoppingCart);
    }

    @Override
    public ShoppingCart findByUserId(Long id) {
        return shoppingCartRepository.findShoppingCartByUserId(id);
    }
}
