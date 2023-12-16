package application.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderRequestShippingAddressDto {
    @NotNull
    private String shippingAddress;
}
