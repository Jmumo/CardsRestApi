package CardsRestApi.Auth;

import CardsRestApi.Config.JwtService;
import CardsRestApi.Dtos.AuthenticationRequest;
import CardsRestApi.Dtos.AuthenticationResponse;
import CardsRestApi.Dtos.RegisterRequest;
import CardsRestApi.Models.Role;
import CardsRestApi.Models.Token;
import CardsRestApi.Models.TokenType;
import CardsRestApi.Models.User;
import CardsRestApi.Repositories.TokenRepo;
import CardsRestApi.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepo tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {

        var user = User.builder()
                .firstname(request.getFirstName())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        var savedUser =  userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        savedUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    private void savedUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .tokentype(TokenType.BEARER)
                .user(user)
                .build();
        tokenRepo.save(token);
    }




    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new UsernameNotFoundException("User Not Found for Authentication"));
        var jwtToken = jwtService.generateToken(user);

        revokeAllUserToken(user);
        savedUserToken(user,jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    private void revokeAllUserToken(User user){
        var validUserTokens = tokenRepo.findAllValidTokenByUser(user.getId());
        if(validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenRepo.saveAll(validUserTokens);
    }
}
