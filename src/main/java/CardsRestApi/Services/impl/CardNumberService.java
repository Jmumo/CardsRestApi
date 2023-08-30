package CardsRestApi.Services.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class CardNumberService {

    private final JdbcTemplate jdbcTemplate;


    public CardNumberService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String generateCardNumber() {
        int updatedValue = jdbcTemplate.update(
                "UPDATE card_number_sequence SET current_value = current_value + 1",
                new Object[]{});

        if (updatedValue != 1) {
            throw new RuntimeException("Failed to update sequence value.");
        }

        int currentValue = jdbcTemplate.queryForObject(
                "SELECT current_value FROM card_number_sequence",
                Integer.class);

        return String.format("%04d", currentValue); // Adjust formatting as needed
    }
}
