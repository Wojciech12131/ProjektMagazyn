package pl.edu.pk.mag.requests.warehouse;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class WarehouseAddress {

    @Email
    @Size(max = 300)
    private String email;
    @NotBlank
    @Pattern(regexp = "[0-9]{9}")
    private String mobile;
    @Size(max = 200)
    @NotBlank
    private String city;

    @NotBlank
    @Size
    private String street;
}
