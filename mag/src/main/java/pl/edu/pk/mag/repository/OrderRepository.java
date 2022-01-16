package pl.edu.pk.mag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pk.mag.repository.entity.UserOrder;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<UserOrder, Long> {

    List<UserOrder> getOrderByWarehouseId(Long id);

    List<UserOrder> getOrderByUserId(Long id);

    Optional<UserOrder> getOrderByWarehouseIdAndId(Long warehouseId, Long id);
}
