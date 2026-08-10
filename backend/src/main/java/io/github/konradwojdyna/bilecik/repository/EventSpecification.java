package io.github.konradwojdyna.bilecik.repository;

import io.github.konradwojdyna.bilecik.entity.Event;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class EventSpecification {

    private EventSpecification() {}

    public static Specification<Event> hasCity(String city){

        if(!StringUtils.hasText(city)){
           return Specification.unrestricted();
        }

        return (root, query, cb ) ->
                cb.equal(cb.lower(root.get("city")), city.toLowerCase());
    }

    public static Specification<Event> hasCategory(String category){

        if(!StringUtils.hasText(category)){
            return Specification.unrestricted();
        }

        return  (root, query, cb) ->
            cb.equal(cb.lower(root.get("category")), category.toLowerCase());
    }
}
