package application.service;

import application.model.CartItem;

public interface CartItemService {
    CartItem findById(Long id);

    void delete(Long id);
}
