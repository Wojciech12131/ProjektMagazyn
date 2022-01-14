package pl.edu.pk.auth.filter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import pl.edu.pk.auth.config.CustomWebAuthenticationDetails;
import pl.edu.pk.auth.model.User;

import java.security.Principal;
import java.util.Map;

@Aspect
@Component
public class AspectTest {

    @Before(value = "execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.postAccessToken(..)) && args(principal,parameters)", argNames = "joinPoint,principal,parameters")
    public void dod(JoinPoint joinPoint, Principal principal, Map<String, String> parameters) {
        if (principal instanceof OAuth2Authentication authentication)
            if (authentication.getPrincipal() != null && authentication.getPrincipal() instanceof User user) {
                if (user.getUsername() != null)
                    parameters.put("username", user.getUsername());
                if(authentication.getUserAuthentication()!=null&& authentication.getUserAuthentication().getDetails() instanceof CustomWebAuthenticationDetails details)
                parameters.put("password", details.getPassword());
            }
    }
}
