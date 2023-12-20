package application.dto.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserLoginResponseDto {
    private String token;

    @JsonCreator
    public UserLoginResponseDto(@JsonProperty("token") String token) {
        this.token = token;
    }
}
