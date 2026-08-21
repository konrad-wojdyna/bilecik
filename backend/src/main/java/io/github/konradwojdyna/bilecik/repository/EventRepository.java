package io.github.konradwojdyna.bilecik.repository;


import io.github.konradwojdyna.bilecik.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {}
