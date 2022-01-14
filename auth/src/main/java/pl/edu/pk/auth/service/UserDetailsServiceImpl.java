package pl.edu.pk.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.edu.pk.auth.exception.UserNotExist;
import pl.edu.pk.auth.model.User;

import javax.annotation.PostConstruct;

@Service(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountStatusUserDetailsChecker accountStatusUserDetailsChecker = new AccountStatusUserDetailsChecker();

    @Value("${service.accountManager.accountServiceId}")
    private String accountServiceId;

    @Autowired
    private RestTemplate restTemplate;

    private String accountServiceUrl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void init() {
        accountServiceUrl = "http://" + accountServiceId + "/mag/user/byName/";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String url = accountServiceUrl + username;
        ResponseEntity<User> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(url, User.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new UsernameNotFoundException(username);
        } catch (Exception e) {
            throw new AuthorizationServiceException("Problem z serwisem autoryzacji, proszę spróbować później");
        }
        User user = responseEntity.getBody();
        if (user != null) {
            accountStatusUserDetailsChecker.check(user);
        } else {
            throw new UserNotExist("Nie znaleziono użytkownika o podanej nazwie: " + username);
        }
        return user;
    }
}
