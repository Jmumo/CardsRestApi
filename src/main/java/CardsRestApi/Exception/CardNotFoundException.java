package CardsRestApi.Exception;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(String cardNumber) {
            super("Card not found with ID: " + cardNumber);
    }
}
