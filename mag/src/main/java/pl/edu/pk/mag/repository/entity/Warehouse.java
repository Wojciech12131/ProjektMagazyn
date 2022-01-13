package pl.edu.pk.mag.repository.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serial;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Warehouse extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 11322512L;

    @Column(unique = true, length = 100)
    private String code;

    private String description;

    @JoinColumn(name = "address_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY, targetEntity = Address.class, cascade = CascadeType.PERSIST)
    private Address address;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = WarehouseGroup.class, cascade = CascadeType.PERSIST)
    private Set<WarehouseGroup> warehouseGroup;


}
