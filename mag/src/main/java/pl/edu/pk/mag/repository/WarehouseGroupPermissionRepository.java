package pl.edu.pk.mag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pk.mag.repository.entity.WPermission;

public interface WarehouseGroupPermissionRepository extends JpaRepository<WPermission, Long> {
}
