package CardsRestApi.Services.impl;

import CardsRestApi.Models.Card;
import CardsRestApi.Models.Status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;

import static org.mockito.Mockito.*;

public class FilterSpecificationsTest {

    @Mock
    private Root root;
    @Mock
    private CriteriaQuery query;
    @Mock
    private CriteriaBuilder criteriaBuilder;

    private FilterSpecifications<Card> filterSpecifications;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        filterSpecifications = new FilterSpecifications<>();
    }

    @Test
    public void testSearchSpecification() {
        String name = "exampleName";
        String color = "#123456";
        LocalDate date = LocalDate.now();
        Status status = Status.TODO;

        Specification<Card> specification = filterSpecifications.searchSpecification(name, color, date, status);
        List<Predicate> predicates = (List<Predicate>) specification.toPredicate(root, query, criteriaBuilder);
        verify(criteriaBuilder, times(1)).and(any());
    }
}