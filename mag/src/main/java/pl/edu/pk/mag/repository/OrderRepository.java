package pl.edu.pk.mag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pk.mag.repository.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> getOrderByWarehouseId(Long id);

    List<Order> getOrderByUserId(Long id);

    Optional<Order> getOrderByWarehouseIdAndId(Long warehouseId, Long id);
}
