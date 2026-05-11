package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT COUNT(e) > 0 FROM Event e WHERE e.category.id = :catId")
    boolean existsByCategoryId(@Param("catId") Long catId);

    List<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long initiatorId);

    @Query("SELECT e FROM Event e " +
            "WHERE (:users IS NULL OR e.initiator.id IN :users) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (CAST(:rangeStart AS timestamp) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (CAST(:rangeEnd AS timestamp) IS NULL OR e.eventDate <= :rangeEnd)")
    List<Event> findEventsByAdmin(
            @Param("users") List<Long> users,
            @Param("states") List<EventState> states,
            @Param("categories") List<Long> categories,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable);

    @Query(value = "SELECT * FROM events e " +
            "WHERE e.state = :state " +
            "AND (:text IS NULL OR (e.annotation ILIKE CONCAT('%', :text, '%') OR e.description ILIKE CONCAT('%', :text, '%'))) " +
            "AND (:categories IS NULL OR e.category_id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (e.event_date >= :rangeStart) " +
            "AND (e.event_date <= :rangeEnd) " +
            "AND (:onlyAvailable = false OR e.participant_limit = 0 OR e.confirmed_requests < e.participant_limit)",
            nativeQuery = true)
    List<Event> findPublishedEvents(
            @Param("state") String state,   // <-- теперь String, не EventState
            @Param("text") String text,
            @Param("categories") List<Long> categories,
            @Param("paid") Boolean paid,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            @Param("onlyAvailable") Boolean onlyAvailable,
            Pageable pageable);
    Optional<Event> findByIdAndState(Long id, EventState state);
}