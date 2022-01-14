package pl.edu.pk.mag.responses;

import lombok.Data;
import pl.edu.pk.mag.repository.entity.Role;
import pl.edu.pk.mag.repository.entity.User;

import java.util.Set;

@Data
public class UserToLogin {

    private String email;
    private String username;
    private String password;
    private boolean enabled;
    private Set<Role> role;

    public static UserToLogin getInstance(User user) {
        UserToLogin userToLogin = new UserToLogin();
        userToLogin.setRole(user.getRoles());
        userToLogin.setUsername(user.getUsername());
        userToLogin.setPassword(user.getPassword());
        userToLogin.setEnabled(user.isEnabled());
        userToLogin.setEmail(user.getAddress().getEmail());
        return userToLogin;
    }


}
