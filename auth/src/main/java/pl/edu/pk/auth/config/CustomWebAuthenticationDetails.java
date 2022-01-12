package pl.edu.pk.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import pl.edu.pk.auth.model.LoginRequest;

import javax.servlet.http.HttpServletRequest;

@Setter
@Getter
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {
    private String username;

    private String password;

    public CustomWebAuthenticationDetails(HttpServletRequest context) {
        super(context);
        String grantType = context.getParameter("grant_type");
        if (grantType != null && grantType.equals("password")) {
            LoginRequest loginRequest = (LoginRequest) context.getAttribute("loginRequest");
            if (loginRequest != null) {
                this.username = loginRequest.getUsername();
                this.password = loginRequest.getPassword();
            }
        }
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
