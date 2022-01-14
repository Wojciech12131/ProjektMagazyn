package pl.edu.pk.mag.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseMembers {

    private String username;

    private List<String> warehousePermissions;
}
