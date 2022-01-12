package pl.edu.pk.mag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pk.mag.repository.entity.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
}
