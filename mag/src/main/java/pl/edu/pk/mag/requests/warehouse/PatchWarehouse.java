package pl.edu.pk.mag.requests.warehouse;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class PatchWarehouse {
    @Size(max = 1000)
    private String description;

    private WarehouseAddress address;
}
