package CardsRestApi.Controllers;

import CardsRestApi.Dtos.CardRequestDto;
import CardsRestApi.Dtos.UpdateCardDto;
import CardsRestApi.Models.Card;
import CardsRestApi.Models.Status;
import CardsRestApi.Services.CardService;
import CardsRestApi.Services.impl.FilterSpecifications;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cards")
@PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
public class CardController {

   private final CardService cardService;



    public CardController(CardService cardService, FilterSpecifications<Card> filterSpecifications) {
        this.cardService = cardService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
    @PostMapping("/save")
    ResponseEntity<Card> save(@RequestBody @Valid CardRequestDto cardRequestDto){
      return ResponseEntity.ok(cardService.saveCard(cardRequestDto));
    }
    @PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
    @GetMapping("/getCards")
    ResponseEntity<List<Card>> getCards(){
        return ResponseEntity.ok(cardService.getCards());
    }

    @PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
    @GetMapping("/getCard/{cardNumber}")
    ResponseEntity<Card> getCard( @PathVariable String cardNumber){
        return ResponseEntity.ok(cardService.getCard(cardNumber));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
    @DeleteMapping("/deleteCard/{cardNumber}")
    ResponseEntity<String> deleteCard( @PathVariable String cardNumber){
        return ResponseEntity.ok(cardService.deleteCard(cardNumber));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
    @DeleteMapping("/updateCard/{cardNumber}")
    ResponseEntity<Card>updateCard( @PathVariable @Valid UpdateCardDto updateCardDto){
        return ResponseEntity.ok(cardService.updateCard(updateCardDto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MEMBER')")
    @GetMapping("/cards")
    public List<Card> searchCard(@RequestParam(required = false) String name,
                                 @RequestParam(required = false) String color,
                                 @RequestParam(required = false) LocalDate createdDate,
                                 @RequestParam(required = false) Status status,
                                 @RequestParam(required = false) String sort,
                                 @RequestParam(required = false) Integer pageNumber,
                                 @RequestParam(required = false) Integer pageSize,
                                 @RequestParam(required = false) Sort.Direction Sortdirection
                                 ){
        return cardService.searchCard(name,color,createdDate,status,sort,pageNumber,pageSize,Sortdirection);
    }






}
