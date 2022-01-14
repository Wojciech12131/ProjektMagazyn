package pl.edu.pk.mag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pk.mag.repository.entity.WPermission;

import java.util.Optional;

public interface WarehouseGroupPermissionRepository extends JpaRepository<WPermission, Long> {

    Optional<WPermission> getWPermissionByCode(String code);
}
