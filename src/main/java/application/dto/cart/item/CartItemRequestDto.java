package application.dto.cart.item;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NonNull;

@Data
public class CartItemRequestDto {
    @NonNull
    private Long bookId;
    @NonNull
    @Min(1)
    private Integer quantity;
}
