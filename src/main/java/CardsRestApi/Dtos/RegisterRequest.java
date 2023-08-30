package CardsRestApi.Dtos;


import CardsRestApi.Models.Card;
import CardsRestApi.Models.Role;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String lastname;
    private String email;
    private String password;
    private Role role;
    private List<Card> cards;

}
