package pl.edu.pk.mag.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.pk.mag.repository.entity.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private String code;
    private String description;

    public ProductResponse(Product product) {
        this.code = product.getCode();
        this.description = product.getDescription();
    }
}
