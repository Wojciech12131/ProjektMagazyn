package pl.edu.pk.mag.requests;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class BasketItemRequest {
    @NotNull
    private String productCode;

    private BigDecimal quantity = new BigDecimal("1.000");
}
