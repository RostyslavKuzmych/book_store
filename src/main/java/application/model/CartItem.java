package application.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@SQLDelete(sql = "UPDATE cart_items SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Column(nullable = false)
    private ShoppingCart shoppingCart;

    @ManyToOne
    @Column(nullable = false)
    private Book book;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private boolean isDeleted = false;

    public CartItem() {
    }
}
