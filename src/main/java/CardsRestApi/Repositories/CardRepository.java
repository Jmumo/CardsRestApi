package CardsRestApi.Repositories;

import CardsRestApi.Models.Card;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card,Long> {


    List<Card> findAll(Specification<Card> cardSpecification);

    List<Card> findAll(Specification<Card> cardSpecification, Pageable pageable);
}
