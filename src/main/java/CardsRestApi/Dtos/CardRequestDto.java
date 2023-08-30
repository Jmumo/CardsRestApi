package CardsRestApi.Dtos;


import CardsRestApi.Models.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardRequestDto {
    private String Name;
    private String description;
    private String color;
}
