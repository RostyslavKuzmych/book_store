package application.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequestShippingAddressDto {
    @NotNull
    private String shippingAddress;
}
