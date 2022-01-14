package pl.edu.pk.mag.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {
    private String email;
    private String mobile;
    private String city;
    private String street;
}
