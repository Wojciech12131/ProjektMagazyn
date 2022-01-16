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

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            joinColumns = {@JoinColumn(name = "warehouse_group_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "wpermission_id", referencedColumnName = "id")},
            name = "GROUP_PERMISSION"
    )
    private List<WPermission> wPermissions;

}
