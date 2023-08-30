package CardsRestApi.Repositories;



import CardsRestApi.Models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepo extends JpaRepository<Token,Long> {

    Optional<Token> findByToken(String token);


    @Query("select t from Token t inner join User u on t.user.id = u.id where u.id = :UserId and (t.expired = false and t.revoked = false)")
    List<Token> findAllValidTokenByUser(Long UserId);

}
