package pl.edu.pk.mag.responses;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pl.edu.pk.mag.repository.entity.User;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserToLogin {

    private String email;
    private String username;
    private String password;
    private boolean enabled;
    private Set<GrantedAuthority> authorities;

    public static UserToLogin getInstance(User user) {
        UserToLogin userToLogin = new UserToLogin();
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (user.getRoles() != null) {
            user.getRoles().forEach(r -> {
                authorities.add(new SimpleGrantedAuthority(r.getCode()));
                r.getPermissions().forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getCode())));
            });
        }
        userToLogin.setAuthorities(authorities);
        userToLogin.setUsername(user.getUsername());
        userToLogin.setPassword(user.getPassword());
        userToLogin.setEnabled(user.isEnabled());
        userToLogin.setEmail(user.getAddress().getEmail());
        return userToLogin;
    }


}
