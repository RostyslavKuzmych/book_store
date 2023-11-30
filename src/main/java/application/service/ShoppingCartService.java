package application.service;

import application.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart save(ShoppingCart shoppingCart);

    ShoppingCart findByUserId(Long id);
}
