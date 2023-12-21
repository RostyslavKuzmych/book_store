package application.dto.order;

import application.model.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderRequestStatusDto {
    @NotNull
    private Status status;
}
