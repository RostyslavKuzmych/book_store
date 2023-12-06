package application.dto.order;

import application.model.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequestStatusDto {
    @NotNull
    private Status status;
}
