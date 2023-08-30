package CardsRestApi.Services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import CardsRestApi.Dtos.CardRequestDto;
import CardsRestApi.Models.Card;
import CardsRestApi.Models.Role;
import CardsRestApi.Models.Status;
import CardsRestApi.Models.User;
import CardsRestApi.Repositories.CardRepository;
import CardsRestApi.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


class CardServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private CardNumberService cardNumberService;

    @Mock
    private CardRepository cardRepository;




    @Mock
    private FilterSpecifications filterSpecifications;

    @InjectMocks
    private CardService cardService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveCardWithValidData() {
        // Prepare mock data and behavior
        CardRequestDto requestDto = new CardRequestDto();
        requestDto.setName("Card Name");
        requestDto.setDescription("Card Description");
        requestDto.setColor("123456");

        User mockUser = new User();
        mockUser.setEmail("user@example.com");

        Card card = new Card();
        card.setName("TestCArd");
        card.setCardNumber("CARD1001");

        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getName()).thenReturn(mockUser.getEmail());

        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);

        SecurityContextHolder.setContext(mockSecurityContext);

        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(Optional.of(mockUser));
        when(cardNumberService.generateCardNumber()).thenReturn("1001");
        Mockito.when(cardRepository.save(any())).thenReturn(card);


        var savedCard = cardService.saveCard(requestDto);

        assertNotNull(savedCard);
       assertEquals("TestCArd", savedCard.getName());
        assertEquals("CARD1001", savedCard.getCardNumber());
        verify(cardRepository, times(1)).save(any());
    }


    @Test
    public void testGetCardsForAdmin() {
        // Prepare mock data
        User adminUser = new User();
        adminUser.setEmail("admin@example.com");
        adminUser.setRole(Role.ADMIN);

        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getName()).thenReturn(adminUser.getEmail());

        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);

        SecurityContextHolder.setContext(mockSecurityContext);

        List<Card> mockCardList = Arrays.asList(new Card(), new Card());

        when(userRepository.findByEmail(adminUser.getEmail())).thenReturn(Optional.of(adminUser));
        when(cardRepository.findAll()).thenReturn(mockCardList);

        // Test the service method
        List<Card> result = cardService.getCards();

        // Assertions
        assertEquals(mockCardList, result);
    }


    @Test
    public void testGetCardsForNonAdminUser() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setRole(Role.MEMBER);

        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getName()).thenReturn(user.getEmail());

        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);

        SecurityContextHolder.setContext(mockSecurityContext);

        List<Card> mockCardList = Arrays.asList(new Card(), new Card());

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(cardRepository.findAll()).thenReturn(mockCardList);

        // Test the service method
        List<Card> result = cardService.getCards();

        // Assertions
        assertEquals(mockCardList, result);
    }


    @Test
    public void testDeleteCardFound() {
        // Prepare mock data
        User user = new User();
        user.setEmail("user@example.com");

        Card mockCard = new Card();
        mockCard.setCardNumber("CARD123");

        // Mock the behavior of userRepository to return a user with a card
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        //when().thenReturn(Arrays.asList(mockCard));

        // Test the service method
        String result = cardService.deleteCard("CARD123");

        // Assertions
        assertEquals("Card Deleted Successfully", result);
        verify(cardRepository, times(1)).delete(mockCard);
    }

    // Prepare mock data
    @Test
    public void testSearchCardWithDefaults() {
        // Prepare mock data
        Specification<Card> mockSpecification = mock(Specification.class);
        List<Card> mockCardList = Arrays.asList(new Card(), new Card());

        when(filterSpecifications.searchSpecification(null, null, null, null)).thenReturn(mockSpecification);
        when(cardRepository.findAll(mockSpecification, PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "createdDate")))).thenReturn(mockCardList);

        // Test the service method
        List<Card> result = cardService.searchCard(null, null, null, null, null, null, null, null);

        // Assertions
        assertEquals(mockCardList, result);
    }


    @Test
    public void testSearchCardWithCustomValues() {
        // Prepare mock data
        Specification<Card> mockSpecification = mock(Specification.class);
        List<Card> mockCardList = Arrays.asList(new Card(), new Card());

        when(filterSpecifications.searchSpecification("Test", "#123456", LocalDate.now(), Status.TODO)).thenReturn(mockSpecification);
        when(cardRepository.findAll(mockSpecification, PageRequest.of(1, 10, Sort.by(Sort.Direction.DESC, "createdDate")))).thenReturn(mockCardList);

        // Test the service method
        List<Card> result = cardService.searchCard("Test", "#123456", LocalDate.now(), Status.TODO, null, 1, 10, Sort.Direction.DESC);

        // Assertions
        assertEquals(mockCardList, result);
    }






}

