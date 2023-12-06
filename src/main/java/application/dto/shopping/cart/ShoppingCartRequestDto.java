package application.dto.shopping.cart;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ShoppingCartRequestDto {
    @Positive
    private Integer quantity;
}
