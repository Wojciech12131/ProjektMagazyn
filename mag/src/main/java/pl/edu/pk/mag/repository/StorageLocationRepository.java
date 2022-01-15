package pl.edu.pk.mag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pk.mag.repository.entity.StorageLocation;

public interface StorageLocationRepository extends JpaRepository<StorageLocation, Long> {
}
