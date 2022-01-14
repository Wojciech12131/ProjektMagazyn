package pl.edu.pk.mag.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseListResponse {
    private String code;
    private String description;
    private AddressResponse address;
}
