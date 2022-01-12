package pl.edu.pk.mag.requests;

import lombok.Data;
import pl.edu.pk.mag.repository.entity.Address;
import pl.edu.pk.mag.repository.entity.User;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserRegistration {

    @NotBlank(message = "Nazwa użytkownika powinna być podana")
    @Size(max = 50, message = "Maksymalna długość znaków dla pola username wynosi 50.")
    private String username;

    @NotBlank(message = "Hasło powinno być podane.")
    @Size(max = 50, message = "Maksymalna długość znaków dla pola password wynosi 50.")
    private String password;

    @NotNull(message = "Adress powinien być podany.")
    @Valid
    private UserAddressReg address;

    public User convertToUser() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEnabled(true);
        Address address = new Address(getAddress().getEmail(), getAddress().getMobile());
        user.setAddress(address);
        return user;
    }
}
