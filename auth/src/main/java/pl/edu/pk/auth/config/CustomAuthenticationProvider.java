package pl.edu.pk.auth.config;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import pl.edu.pk.auth.exception.UserNotExist;
import pl.edu.pk.auth.model.User;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication auth)
            throws AuthenticationException {
        if (!auth.getDetails().getClass().getSimpleName().equals("LinkedHashMap")) {
            User user;
            try {
                user = (User) getUserDetailsService().loadUserByUsername(auth.getName());
            } catch (UserNotExist e) {
                throw new InternalAuthenticationServiceException(e.getMessage());
            }

            if ((user == null)) {
                throw new InternalAuthenticationServiceException("Invalid username or password");
            }

            Authentication result = super.authenticate(auth);
            return new UsernamePasswordAuthenticationToken(
                    user, result.getCredentials(), result.getAuthorities());
        } else {
            return super.authenticate(auth);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
