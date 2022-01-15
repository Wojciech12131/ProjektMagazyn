package pl.edu.pk.mag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pk.mag.repository.entity.Warehouse;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    boolean existsWarehouseByCode(String code);

    Optional<Warehouse> getWarehouseByCode(String code);

    Warehouse getWarehouseById(Long id);

    List<Warehouse> findAll();
}
