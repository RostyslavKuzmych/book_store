package application.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@SQLDelete(sql = "UPDATE shoppingCarts SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Entity
@Data
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.EAGER)
    @NonNull
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(name = "shoppingCarts_cartItems",
            joinColumns = @JoinColumn(name = "shoppingCart_id"),
            inverseJoinColumns = @JoinColumn(name = "cartItem_id"))
    private Set<CartItem> cartItemSet = new HashSet<>();

    @OneToOne
    @NonNull
    private User user;

    @NonNull
    private boolean isDeleted = false;

    public ShoppingCart() {
    }
}
