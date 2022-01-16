package pl.edu.pk.mag.requests.warehouse;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class AddUserToWarehouse {

    @Size(max = 50)
    @NotBlank
    private String username;

    private List<String> warehousePermissions = new ArrayList<>();
}
