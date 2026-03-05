package co.project.api_catalogo.repository;

import co.project.api_catalogo.entity.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, Long> {

    boolean existsByEventId(String eventId);
}
