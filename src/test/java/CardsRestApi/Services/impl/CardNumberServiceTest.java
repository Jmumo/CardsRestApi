package CardsRestApi.Services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CardNumberServiceTest {


        @Mock
        private JdbcTemplate jdbcTemplate;

        private CardNumberService cardNumberService;

        @BeforeEach
        public void setUp() {
            MockitoAnnotations.initMocks(this);
            cardNumberService = new CardNumberService(jdbcTemplate);
        }

        @Test
        public void testGenerateCardNumber() {
            int updatedValue = 1; // Simulate successful update

            when(jdbcTemplate.update(anyString(), Optional.ofNullable(any()))).thenReturn(updatedValue);
            when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class))).thenReturn(1001);

            String cardNumber = cardNumberService.generateCardNumber();

            assertEquals("1001", cardNumber); // Adjust based on the expected result
            verify(jdbcTemplate, times(1)).update(anyString(), Optional.ofNullable(any()));
            verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class));
    }
}