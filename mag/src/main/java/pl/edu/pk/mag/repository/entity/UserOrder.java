package pl.edu.pk.mag.repository.entity;

import lombok.*;
import pl.edu.pk.mag.repository.dto.BasketItem;
import pl.edu.pk.mag.repository.entity.converters.BasketItemConverter;
import pl.edu.pk.mag.repository.entity.converters.OrderStatusConverter;
import pl.edu.pk.mag.repository.entity.enums.OrderStatus;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class UserOrder extends BaseEntity {
    private Long userId;

    private Long warehouseId;

    @Convert(converter = OrderStatusConverter.class)
    private OrderStatus orderStatus;

    @Convert(converter = BasketItemConverter.class)
    @Column(length = 5000)
    private List<BasketItem> basketItem;
}
