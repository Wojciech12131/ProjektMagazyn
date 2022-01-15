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

    @JoinColumn(name = "warehouse_id")
    @Column(name = "warehouse_id")
    private Long warehouseId;

    @JoinColumn(name = "user_id")
    @Column(name = "user_id")
    private Long userId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<WPermission> wPermissions;

}
