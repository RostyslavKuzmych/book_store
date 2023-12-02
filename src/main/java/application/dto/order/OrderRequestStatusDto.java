package application.dto.order;

import application.model.Order;
import lombok.Data;

@Data
public class OrderRequestStatusDto {
    private Order.Status status;
}
