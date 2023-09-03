package CardsRestApi.Services.impl;

import CardsRestApi.Dtos.CardRequestDto;
import CardsRestApi.Dtos.UpdateCardDto;
import CardsRestApi.Exception.CardNotFoundException;
import CardsRestApi.Models.Card;
import CardsRestApi.Models.Role;
import CardsRestApi.Models.Status;
import CardsRestApi.Models.User;
import CardsRestApi.Repositories.CardRepository;
import CardsRestApi.Repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CardService implements CardsRestApi.Services.CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardNumberService cardNumberService;
    private final FilterSpecifications<Card> filterSpecifications;



    public CardService(CardRepository cardRepository, UserRepository userRepository, CardNumberService cardNumberService, FilterSpecifications<Card> filterSpecifications, CardSpecificationBuilder cardCardSpecificationBuilder) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.cardNumberService = cardNumberService;
        this.filterSpecifications = filterSpecifications;

    }


    @Override
    public Card saveCard(CardRequestDto cardRequestDto) {

        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        var color = validateColor(cardRequestDto.getColor());

        var cardNumber = "CARD".concat(cardNumberService.generateCardNumber());

        if(user.isPresent()){
            			var card = Card.builder()
					.description(cardRequestDto.getDescription())
					.color(color)
					.status(Status.TODO)
					.name(cardRequestDto.getName())
					.user(user.get())
                    .cardNumber(cardNumber)
                    .createdDate(LocalDate.now())
					.build();

            return cardRepository.save(card);
        }
        return null;
    }

    @Override
    public List<Card> getCards() {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();

        if(user.getRole().equals(Role.ADMIN)){
               return cardRepository.findAll();
        }else {
            return user.getCards();
        }

    }

    @Override
    public Card getCard(String cardNumber) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();

        Optional<Card> userCard = user.getCards().stream().filter(card -> (card.getCardNumber().equals(cardNumber))).findFirst();

        return userCard.orElseThrow(()->new CardNotFoundException(cardNumber));
    }

    @Override
    public String deleteCard(String cardNumber) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();

        Optional<Card> userCard = Optional.of(user.getCards().stream().filter(card -> (card.getCardNumber()
                .equals(cardNumber))).findFirst().orElseThrow(()->new CardNotFoundException(cardNumber)));

        cardRepository.delete(userCard.get());

        return "Card Deleted Successfully";
    }

    @Override
    public Card updateCard(UpdateCardDto updateCardDto) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();

        Optional<Card> userCard = user.getCards().stream().filter(card -> (card.getCardNumber().equals(updateCardDto.getCardNumber()))).findFirst();

        if(userCard.isPresent()){
            if (updateCardDto.getDescription() != null) {
                userCard.get().setDescription(updateCardDto.getDescription());
            }

            if (updateCardDto.getColor() != null) {
                userCard.get().setColor(updateCardDto.getColor());
            }

            if (updateCardDto.getStatus() != null) {
                userCard.get().setStatus(updateCardDto.getStatus());
            }
            if (updateCardDto.getName() != null){
                userCard.get().setName(updateCardDto.getName());
            }

            return cardRepository.save(userCard.get());
        }else {
            throw new CardNotFoundException(updateCardDto.getCardNumber());
        }

    }



    @Override
    public List<Card> searchCard(String name, String color, LocalDate date, Status status, String sort, Integer pageNumber, Integer pageSize, Sort.Direction Sortdirection) {
        Specification<Card> cardSpecification = filterSpecifications.searchSpecification(name, color,date,status);

        if(pageNumber == null){
            pageNumber = 0;
        }

        if(pageSize == null){
            pageSize =1;
        }
        if(sort == null){
            sort = "createdDate";
        }

        if(Sortdirection == null){
           Sortdirection = Sort.Direction.ASC;
        }else {
            Sortdirection = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sortdirection,sort));
        List<Card> cardList = cardRepository.findAll(cardSpecification,pageable);

        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();

        return cardList.stream().filter(card->(card.getUser().equals(user))).collect(Collectors.toList());
    }

    private String validateColor(String color) {
        if(color.isEmpty()){
            color = "";
        }else {
            color = "#".concat(color);
        }
        return color;
    }
}


