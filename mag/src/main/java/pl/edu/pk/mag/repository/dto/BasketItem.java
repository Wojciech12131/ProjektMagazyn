package pl.edu.pk.mag.repository.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BasketItem {
    private Long productId;

    private BigDecimal quantity = new BigDecimal("1.000");
}
