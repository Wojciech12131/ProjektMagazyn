package pl.edu.pk.mag.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import pl.edu.pk.mag.exceptions.handlers.CustomAccessDeniedHandler;
import pl.edu.pk.mag.exceptions.handlers.CustomAuthenticationEntryPoint;
import pl.edu.pk.mag.exceptions.handlers.ExceptionHandling;
import pl.edu.pk.mag.exceptions.handlers.ResponseErrorHandlerImpl;
import pl.edu.pk.mag.filters.OauthErrorHandleFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private ExceptionHandling exceptionHandling;

    @Autowired
    private OauthErrorHandleFilter oauthErrorHandleFilter;

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint())
                .tokenServices(tokenService())
        ;
        super.configure(resources);
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable().and()
                .addFilterBefore(oauthErrorHandleFilter, AbstractPreAuthenticatedProcessingFilter.class)
                .httpBasic().disable().formLogin().disable()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint()).and().exceptionHandling().and()
                .authorizeRequests()
                .antMatchers("/mag/user/byName/**").permitAll()
                .antMatchers(HttpMethod.POST, "/mag/user").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/**").authenticated();
    }

    @Bean
    public ResourceServerTokenServices tokenService() {
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setRestTemplate(restTemplate());
        tokenServices.setClientId("client");
        tokenServices.setClientSecret("client");
        tokenServices.setCheckTokenEndpointUrl("http://auth-service/oauth/check_token");
        return tokenServices;
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(errorHandler());
        return restTemplate;
    }

    @Bean
    public ResponseErrorHandler errorHandler() {
        return new ResponseErrorHandlerImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }
}
