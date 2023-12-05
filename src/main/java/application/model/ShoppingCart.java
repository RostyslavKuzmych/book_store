package application.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@SQLDelete(sql = "UPDATE shopping_carts SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Entity
@Data
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "shoppingCart")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<CartItem> cartItemSet = new HashSet<>();

    @OneToOne
    @Column(nullable = false)
    private User user;

    @Column(nullable = false)
    private boolean isDeleted = false;

    public ShoppingCart() {
    }
}
