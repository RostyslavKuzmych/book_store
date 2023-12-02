package application.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;

@Entity
@Data
@SQLDelete(sql = "UPDATE order_items SET is_deleted = true")
@Where(clause = "is_deleted = false")
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @NonNull
    private Order order;

    @ManyToOne
    @NonNull
    private Book book;

    @NonNull
    private Integer quantity;

    @NonNull
    private BigDecimal price;

    @NonNull
    private boolean is_deleted = false;

    public OrderItem() {
    }
}
