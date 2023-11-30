package application.model;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@SQLDelete(sql = "UPDATE shoppingCarts SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Table(name = "shoppingCarts")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @NonNull
    @JoinTable(name = "shoppingCarts_cartItems",
    joinColumns = @JoinColumn(name = "shoppingCart_id"),
            inverseJoinColumns = @JoinColumn(name = "cartItem_id"))
    private Set<CartItem> cartItemSet = new HashSet<>();

    @OneToOne
    @NonNull
    private User user;
    @NonNull
    private boolean is_deleted = false;

    public ShoppingCart() {
    }
}
