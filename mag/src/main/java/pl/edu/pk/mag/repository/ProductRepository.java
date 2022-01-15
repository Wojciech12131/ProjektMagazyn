package pl.edu.pk.mag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pk.mag.repository.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
