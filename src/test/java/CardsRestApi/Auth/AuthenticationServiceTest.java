package CardsRestApi.Auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import CardsRestApi.Config.JwtService;
import CardsRestApi.Dtos.AuthenticationRequest;
import CardsRestApi.Dtos.AuthenticationResponse;
import CardsRestApi.Dtos.RegisterRequest;
import CardsRestApi.Models.Card;
import CardsRestApi.Models.Role;
import CardsRestApi.Models.Token;
import CardsRestApi.Models.TokenType;
import CardsRestApi.Models.User;
import CardsRestApi.Repositories.TokenRepo;
import CardsRestApi.Repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AuthenticationService.class})
@ExtendWith(SpringExtension.class)
class AuthenticationServiceTest {
    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationService authenticationService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private TokenRepo tokenRepo;

    @MockBean
    private UserRepository userRepository;


    /**
     * Method under test: {@link AuthenticationService#register(RegisterRequest)}
     */
    @Test
    void testRegister() {
        User user = new User();
        user.setCards(new ArrayList<>());
        user.setEmail("jane.doe@example.org");
        user.setFirstname("Jane");
        user.setId(1L);
        user.setLastname("Doe");
        user.setPassword("iloveyou");
        user.setRole(Role.ADMIN);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);

        User user2 = new User();
        user2.setCards(new ArrayList<>());
        user2.setEmail("jane.doe@example.org");
        user2.setFirstname("Jane");
        user2.setId(1L);
        user2.setLastname("Doe");
        user2.setPassword("iloveyou");
        user2.setRole(Role.ADMIN);

        Token token = new Token();
        token.setExpired(true);
        token.setId(1L);
        token.setRevoked(true);
        token.setToken("ABC123");
        token.setTokentype(TokenType.BEARER);
        token.setUser(user2);
        when(tokenRepo.save(Mockito.<Token>any())).thenReturn(token);
        when(jwtService.generateToken(Mockito.<UserDetails>any())).thenReturn("ABC123");
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");
        assertEquals("ABC123", authenticationService.register(new RegisterRequest()).getToken());
        verify(userRepository).save(Mockito.<User>any());
        verify(tokenRepo).save(Mockito.<Token>any());
        verify(jwtService).generateToken(Mockito.<UserDetails>any());
        verify(passwordEncoder).encode(Mockito.<CharSequence>any());
    }

    /**
     * Method under test: {@link AuthenticationService#register(RegisterRequest)}
     */
    @Test
    void testRegister2() {
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenThrow(new UsernameNotFoundException("Msg"));
        assertThrows(UsernameNotFoundException.class, () -> authenticationService.register(new RegisterRequest()));
        verify(passwordEncoder).encode(Mockito.<CharSequence>any());
    }

    /**
     * Method under test: {@link AuthenticationService#authenticate(AuthenticationRequest)}
     */


    /**
     * Method under test: {@link AuthenticationService#authenticate(AuthenticationRequest)}
     */
    @Test
    void testAuthenticate2() throws AuthenticationException {
        User user = new User();
        user.setCards(new ArrayList<>());
        user.setEmail("jane.doe@example.org");
        user.setFirstname("Jane");
        user.setId(1L);
        user.setLastname("Doe");
        user.setPassword("iloveyou");
        user.setRole(Role.ADMIN);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);

        User user2 = new User();
        user2.setCards(new ArrayList<>());
        user2.setEmail("jane.doe@example.org");
        user2.setFirstname("Jane");
        user2.setId(1L);
        user2.setLastname("Doe");
        user2.setPassword("iloveyou");
        user2.setRole(Role.ADMIN);

        Token token = new Token();
        token.setExpired(true);
        token.setId(1L);
        token.setRevoked(true);
        token.setToken("ABC123");
        token.setTokentype(TokenType.BEARER);
        token.setUser(user2);
        when(tokenRepo.save(Mockito.<Token>any())).thenReturn(token);
        when(tokenRepo.findAllValidTokenByUser(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        when(jwtService.generateToken(Mockito.<UserDetails>any())).thenReturn("ABC123");
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        AuthenticationRequest request = mock(AuthenticationRequest.class);
        when(request.getEmail()).thenReturn("jane.doe@example.org");
        when(request.getPassword()).thenReturn("iloveyou");
        assertEquals("ABC123", authenticationService.authenticate(request).getToken());
        verify(userRepository).findByEmail(Mockito.<String>any());
        verify(tokenRepo).save(Mockito.<Token>any());
        verify(tokenRepo).findAllValidTokenByUser(Mockito.<Long>any());
        verify(jwtService).generateToken(Mockito.<UserDetails>any());
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
        verify(request, atLeast(1)).getEmail();
        verify(request).getPassword();
    }

    /**
     * Method under test: {@link AuthenticationService#authenticate(AuthenticationRequest)}
     */
    @Test
    void testAuthenticate3() throws AuthenticationException {
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        doNothing().when(user).setCards(Mockito.<List<Card>>any());
        doNothing().when(user).setEmail(Mockito.<String>any());
        doNothing().when(user).setFirstname(Mockito.<String>any());
        doNothing().when(user).setId(Mockito.<Long>any());
        doNothing().when(user).setLastname(Mockito.<String>any());
        doNothing().when(user).setPassword(Mockito.<String>any());
        doNothing().when(user).setRole(Mockito.<Role>any());
        user.setCards(new ArrayList<>());
        user.setEmail("jane.doe@example.org");
        user.setFirstname("Jane");
        user.setId(1L);
        user.setLastname("Doe");
        user.setPassword("iloveyou");
        user.setRole(Role.ADMIN);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);

        User user2 = new User();
        user2.setCards(new ArrayList<>());
        user2.setEmail("jane.doe@example.org");
        user2.setFirstname("Jane");
        user2.setId(1L);
        user2.setLastname("Doe");
        user2.setPassword("iloveyou");
        user2.setRole(Role.ADMIN);

        Token token = new Token();
        token.setExpired(true);
        token.setId(1L);
        token.setRevoked(true);
        token.setToken("ABC123");
        token.setTokentype(TokenType.BEARER);
        token.setUser(user2);
        when(tokenRepo.save(Mockito.<Token>any())).thenReturn(token);
        when(tokenRepo.findAllValidTokenByUser(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        when(jwtService.generateToken(Mockito.<UserDetails>any())).thenReturn("ABC123");
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        AuthenticationRequest request = mock(AuthenticationRequest.class);
        when(request.getEmail()).thenReturn("jane.doe@example.org");
        when(request.getPassword()).thenReturn("iloveyou");
        assertEquals("ABC123", authenticationService.authenticate(request).getToken());
        verify(userRepository).findByEmail(Mockito.<String>any());
        verify(user).getId();
        verify(user).setCards(Mockito.<List<Card>>any());
        verify(user).setEmail(Mockito.<String>any());
        verify(user).setFirstname(Mockito.<String>any());
        verify(user).setId(Mockito.<Long>any());
        verify(user).setLastname(Mockito.<String>any());
        verify(user).setPassword(Mockito.<String>any());
        verify(user).setRole(Mockito.<Role>any());
        verify(tokenRepo).save(Mockito.<Token>any());
        verify(tokenRepo).findAllValidTokenByUser(Mockito.<Long>any());
        verify(jwtService).generateToken(Mockito.<UserDetails>any());
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
        verify(request, atLeast(1)).getEmail();
        verify(request).getPassword();
    }

    /**
     * Method under test: {@link AuthenticationService#authenticate(AuthenticationRequest)}
     */
    @Test
    void testAuthenticate4() throws AuthenticationException {
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(Optional.empty());
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        AuthenticationRequest request = mock(AuthenticationRequest.class);
        when(request.getEmail()).thenReturn("jane.doe@example.org");
        when(request.getPassword()).thenReturn("iloveyou");
        assertThrows(UsernameNotFoundException.class, () -> authenticationService.authenticate(request));
        verify(userRepository).findByEmail(Mockito.<String>any());
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
        verify(request, atLeast(1)).getEmail();
        verify(request).getPassword();
    }

    /**
     * Method under test: {@link AuthenticationService#authenticate(AuthenticationRequest)}
     */
    @Test
    void testAuthenticate5() throws AuthenticationException {
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        doNothing().when(user).setCards(Mockito.<List<Card>>any());
        doNothing().when(user).setEmail(Mockito.<String>any());
        doNothing().when(user).setFirstname(Mockito.<String>any());
        doNothing().when(user).setId(Mockito.<Long>any());
        doNothing().when(user).setLastname(Mockito.<String>any());
        doNothing().when(user).setPassword(Mockito.<String>any());
        doNothing().when(user).setRole(Mockito.<Role>any());
        user.setCards(new ArrayList<>());
        user.setEmail("jane.doe@example.org");
        user.setFirstname("Jane");
        user.setId(1L);
        user.setLastname("Doe");
        user.setPassword("iloveyou");
        user.setRole(Role.ADMIN);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);

        User user2 = new User();
        user2.setCards(new ArrayList<>());
        user2.setEmail("jane.doe@example.org");
        user2.setFirstname("Jane");
        user2.setId(1L);
        user2.setLastname("Doe");
        user2.setPassword("iloveyou");
        user2.setRole(Role.ADMIN);

        Token token = new Token();
        token.setExpired(true);
        token.setId(1L);
        token.setRevoked(true);
        token.setToken("ABC123");
        token.setTokentype(TokenType.BEARER);
        token.setUser(user2);

        User user3 = new User();
        user3.setCards(new ArrayList<>());
        user3.setEmail("jane.doe@example.org");
        user3.setFirstname("Jane");
        user3.setId(1L);
        user3.setLastname("Doe");
        user3.setPassword("iloveyou");
        user3.setRole(Role.ADMIN);

        Token token2 = new Token();
        token2.setExpired(true);
        token2.setId(1L);
        token2.setRevoked(true);
        token2.setToken("ABC123");
        token2.setTokentype(TokenType.BEARER);
        token2.setUser(user3);

        ArrayList<Token> tokenList = new ArrayList<>();
        tokenList.add(token2);
        when(tokenRepo.saveAll(Mockito.<Iterable<Token>>any())).thenReturn(new ArrayList<>());
        when(tokenRepo.save(Mockito.<Token>any())).thenReturn(token);
        when(tokenRepo.findAllValidTokenByUser(Mockito.<Long>any())).thenReturn(tokenList);
        when(jwtService.generateToken(Mockito.<UserDetails>any())).thenReturn("ABC123");
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        AuthenticationRequest request = mock(AuthenticationRequest.class);
        when(request.getEmail()).thenReturn("jane.doe@example.org");
        when(request.getPassword()).thenReturn("iloveyou");
        assertEquals("ABC123", authenticationService.authenticate(request).getToken());
        verify(userRepository).findByEmail(Mockito.<String>any());
        verify(user).getId();
        verify(user).setCards(Mockito.<List<Card>>any());
        verify(user).setEmail(Mockito.<String>any());
        verify(user).setFirstname(Mockito.<String>any());
        verify(user).setId(Mockito.<Long>any());
        verify(user).setLastname(Mockito.<String>any());
        verify(user).setPassword(Mockito.<String>any());
        verify(user).setRole(Mockito.<Role>any());
        verify(tokenRepo).save(Mockito.<Token>any());
        verify(tokenRepo).findAllValidTokenByUser(Mockito.<Long>any());
        verify(tokenRepo).saveAll(Mockito.<Iterable<Token>>any());
        verify(jwtService).generateToken(Mockito.<UserDetails>any());
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
        verify(request, atLeast(1)).getEmail();
        verify(request).getPassword();
    }


}

