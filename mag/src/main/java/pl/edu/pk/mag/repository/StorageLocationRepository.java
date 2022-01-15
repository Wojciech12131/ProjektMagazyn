package pl.edu.pk.mag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pk.mag.repository.entity.StorageLocation;

import java.util.List;
import java.util.Optional;

public interface StorageLocationRepository extends JpaRepository<StorageLocation, Long> {

    Optional<StorageLocation> getStorageLocationByCodeAndWarehouseId(String code, Long warehouseId);

    boolean existsStorageLocationByWarehouseIdAndCode(Long warehouseId, String code);

    List<StorageLocation> getStorageLocationByWarehouseIdAndProductId(Long warehouseId, Long productId);
}
