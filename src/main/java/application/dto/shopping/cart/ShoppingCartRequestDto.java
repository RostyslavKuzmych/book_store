package application.dto.shopping.cart;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ShoppingCartRequestDto {
    @Min(1)
    private Integer quantity;
}
