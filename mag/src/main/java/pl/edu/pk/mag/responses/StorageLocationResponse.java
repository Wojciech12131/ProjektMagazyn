package pl.edu.pk.mag.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StorageLocationResponse {
    private String code;
    private BigDecimal quantity;
    private ProductResponse product;
}
