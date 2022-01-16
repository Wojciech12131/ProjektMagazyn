package pl.edu.pk.mag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pk.mag.repository.entity.AuditModification;


public interface AuditModificationRepository extends JpaRepository<AuditModification, Long> {
}
