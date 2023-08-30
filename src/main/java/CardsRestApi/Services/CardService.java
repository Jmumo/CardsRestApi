package CardsRestApi.Services;

import CardsRestApi.Dtos.CardRequestDto;
import CardsRestApi.Dtos.UpdateCardDto;
import CardsRestApi.Models.Card;
import CardsRestApi.Models.Status;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

public interface CardService {
    Card saveCard(CardRequestDto cardRequestDto);

    List<Card> getCards();

    Card getCard(String cardNumber);

    String deleteCard(String cardNumber);

    Card updateCard(UpdateCardDto updateCardDto);

    List<Card> searchCard(String name, String color, LocalDate date, Status status, String sort, Integer pageNumber, Integer PageSize, Sort.Direction Sortdirection);
}
