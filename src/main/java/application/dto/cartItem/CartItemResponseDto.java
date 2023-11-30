package application.dto.cartItem;

import lombok.Data;
import lombok.NonNull;

@Data
public class CartItemResponseDto {
    private Long id;
    private Long bookId;
    private String bookName;
    private Integer quantity;
}
