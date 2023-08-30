package CardsRestApi.Dtos;


import CardsRestApi.Models.Status;
import lombok.Data;

@Data
public class UpdateCardDto {
    private String Name;
    private String description;
    private String color;
    private String cardNumber;
    private Status status;
}
