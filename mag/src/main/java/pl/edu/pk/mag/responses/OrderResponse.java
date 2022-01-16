package pl.edu.pk.mag.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.pk.mag.repository.dto.BasketItem;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long orderId;

    private LocalDateTime createDate;

    private String warehouseCode;

    private String orderStatus;

    private List<BasketItem> basketItems;
}
