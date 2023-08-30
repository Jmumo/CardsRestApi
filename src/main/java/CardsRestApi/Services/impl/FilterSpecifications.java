package CardsRestApi.Services.impl;


import CardsRestApi.Models.Status;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FilterSpecifications<T> {

    public Specification<T> searchSpecification(String name, String color, LocalDate date, Status status) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                if (name != null && !name.isEmpty()) {
                    predicates.add(criteriaBuilder.equal(root.get("name"),name));
                }

                if (color != null && !color.isEmpty()) {
                    predicates.add(criteriaBuilder.equal(root.get("name"),color));
                }

                if (status != null && !status.name().isEmpty()) {
                    predicates.add(criteriaBuilder.equal(root.get("status"),status));
                }

                if (date != null) {
                    predicates.add(criteriaBuilder.equal(root.get("createdDate"),date));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}



