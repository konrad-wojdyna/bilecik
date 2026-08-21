package io.github.konradwojdyna.bilecik.repository;

import io.github.konradwojdyna.bilecik.entity.TicketPool;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketPoolRepository extends JpaRepository<TicketPool,Long> {

    List<TicketPool> findByEventId(Long eventId);

    Optional<TicketPool> findByIdAndEventId(Long id, Long eventId);

    boolean existsByEventIdAndName(Long eventId, String name);

    boolean existsByEventIdAndNameAndIdNot(Long eventId, String name, Long id);
}
