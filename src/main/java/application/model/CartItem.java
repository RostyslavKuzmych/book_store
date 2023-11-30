package application.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

@Table(name = "cartItems")
@Entity
@Data
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Long id;
    @ManyToOne
    @NonNull
    private ShoppingCart shoppingCart;
    @ManyToOne
    @NonNull
    private Book book;
    @NonNull
    private Integer quantity;

    public CartItem() {
    }
}
