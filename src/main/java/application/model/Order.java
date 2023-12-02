package application.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@SQLDelete(sql = "UPDATE orders SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NonNull
    private User user;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private Status status;

    @NonNull
    private BigDecimal total;

    @NonNull
    private LocalDateTime localDateTime;

    @NonNull
    private String shippingAddress;

    @NonNull
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order")
    private Set<OrderItem> itemSet = new HashSet<>();

    @NonNull
    private boolean is_deleted = false;

    public enum Status {
        PROCESSED,
        SHIPPED,
        DELIVERED,
        CANCELLED,
        RETURNED,
        RECEIVED,
        PENDING
    }
}
