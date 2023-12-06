package application.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @NotNull
    private String name;
    @Size(min = 5, message = "should have at least 5 characters")
    private String description;
}
