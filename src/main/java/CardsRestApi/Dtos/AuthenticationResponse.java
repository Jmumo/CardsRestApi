package CardsRestApi.Dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {
    @JsonProperty("Token")
    private String token;

}
