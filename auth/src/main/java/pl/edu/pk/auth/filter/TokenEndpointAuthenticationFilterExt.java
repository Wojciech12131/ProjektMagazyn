package pl.edu.pk.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpointAuthenticationFilter;
import pl.edu.pk.auth.config.CustomWebAuthenticationDetailsSource;
import pl.edu.pk.auth.model.LoginRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TokenEndpointAuthenticationFilterExt extends TokenEndpointAuthenticationFilter {

    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new CustomWebAuthenticationDetailsSource();

    /**
     * @param authenticationManager an AuthenticationManager for the incoming request
     */
    public TokenEndpointAuthenticationFilterExt(AuthenticationManager authenticationManager, OAuth2RequestFactory oAuth2RequestFactory) {
        super(authenticationManager, oAuth2RequestFactory);
    }

    @Override
    protected Authentication extractCredentials(HttpServletRequest request) {
        String grantType = request.getParameter("grant_type");
        if (grantType != null && grantType.equals("password")) {
            LoginRequest loginRequest = null;
            try {
                loginRequest = new ObjectMapper().readValue(request.getReader(), LoginRequest.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            request.setAttribute("loginRequest", loginRequest);
            UsernamePasswordAuthenticationToken result = null;
            if (loginRequest.getUsername() != null) {
                result = new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword());
                result.setDetails(authenticationDetailsSource.buildDetails(request));
            }
            return result;
        }
        return null;
    }
}
