package pl.edu.pk.mag.repository.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Warehouse extends BaseEntity implements Cloneable {
    @Serial
    private static final long serialVersionUID = 11322512L;

    @Column(unique = true, length = 100)
    private String code;

    private String description;

    @JoinColumn(name = "address_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.EAGER, targetEntity = Address.class, cascade = CascadeType.PERSIST)
    private Address address;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "warehouse_id")
    @ToString.Exclude
    private Set<WarehouseGroup> warehouseGroup;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "warehouse_id")
    @ToString.Exclude
    private Set<StorageLocation> storageLocations = new HashSet<>();

    @Override
    public Object clone() {
        Warehouse warehouse = null;
        try {
            warehouse = (Warehouse) super.clone();
            warehouse.setAddress((Address) address.clone());
        } catch (Exception e) {
            warehouse = new Warehouse(code, description, (Address) address.clone(), warehouseGroup, storageLocations);
        }

        return warehouse;
    }

}
