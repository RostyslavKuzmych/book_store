package application.dto.shopping.cart;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NonNull;

@Data
public class ShoppingCartRequestDto {
    @NonNull
    @Min(1)
    private Integer quantity;
}
