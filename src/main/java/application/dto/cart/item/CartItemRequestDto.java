package application.dto.cart.item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class CartItemRequestDto {
    @NotNull
    private Long bookId;
    @Min(1)
    private Integer quantity;
}
