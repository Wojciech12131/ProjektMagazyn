package pl.edu.pk.mag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pk.mag.repository.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
