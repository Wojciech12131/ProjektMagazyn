package pl.edu.pk.mag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pk.mag.repository.entity.Warehouse;

import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    boolean existsWarehouseByCode(String code);

    Optional<Warehouse> getWarehouseByCode(String code);
}
