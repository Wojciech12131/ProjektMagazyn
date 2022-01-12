package pl.edu.pk.mag.requests;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserAddressReg {
    @Pattern(regexp = "[0-9]{9}")
    private String mobile;
    @Email
    @NotBlank
    private String email;
}
