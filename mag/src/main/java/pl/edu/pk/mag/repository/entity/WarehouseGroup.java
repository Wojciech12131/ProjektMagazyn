package pl.edu.pk.mag.repository.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class WarehouseGroup extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private List<WPermission> wPermissions;

}
