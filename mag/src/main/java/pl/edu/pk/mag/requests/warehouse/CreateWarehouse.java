package pl.edu.pk.mag.requests.warehouse;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateWarehouse {

    @Size(max = 100)
    @NotBlank
    private String code;
    @Size(max = 1000)
    private String description;
    @NotNull
    @Valid
    private WarehouseAddress address;
}
