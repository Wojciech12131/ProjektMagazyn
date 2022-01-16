package pl.edu.pk.mag.repository.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class StorageLocation extends BaseEntity implements Cloneable {
    @Column(length = 50)
    private String code;

    @JoinColumn(name = "warehouse_id")
    @Column(name = "warehouse_id")
    private Long warehouseId;

    @JoinColumn(name = "product_id")
    @Column(name = "product_id")
    private Long productId;

    @Column(precision = 19, scale = 3)
    private BigDecimal quantity = new BigDecimal("0.000");

    @Override
    public Object clone() {
        StorageLocation storageLocation;

        try {
            storageLocation = (StorageLocation) super.clone();
        } catch (Exception e) {
            storageLocation = new StorageLocation(code, warehouseId, productId, quantity);
        }
        storageLocation.setQuantity(quantity);
        return storageLocation;
    }
}
