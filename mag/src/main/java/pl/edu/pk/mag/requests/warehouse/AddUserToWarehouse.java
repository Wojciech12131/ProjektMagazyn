package pl.edu.pk.mag.requests.warehouse;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class AddUserToWarehouse {

    @Size(max = 50)
    private String username;

    private List<String> whPermissions = new ArrayList<>();
}
