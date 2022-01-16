package pl.edu.pk.mag.repository.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class BasketItem {
    @NotBlank
    private Long productId;

    private BigDecimal quantity = new BigDecimal("0.000");
}
