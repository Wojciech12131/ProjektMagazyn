package pl.edu.pk.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationDetailsSource authenticationDetailsSource;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new CustomTokenEnhancer();
        converter.setSigningKey("zxcvasdgasdbadsa;usdskavbskjnawoet2084utoauijsnd;l,sd" +
                "a'gksg'jsd;lgnsd'fkhjpseihgjsldfngkmsd'fjgsl;djf'ljsdfjnhs'odfjhsijdh;sdlmh" +
                ";df'hsdf'hkmd'flhjsdfojh;'ejh;kdmf<?ndmjeoihrtpoueshgil;sjd'fgj'dfojh'sdfjg'sdk;lv;ldkf'gjsp'oejh'dsfjhkdsjh;lksdfh'lsdjhs'hsd'fhsdfhsdf" +
                "sfasdf");
        return converter;
    }

    @Bean
    public UrlBasedCorsConfigurationSource configurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://localhost:3000"));
        corsConfiguration.setAllowedMethods(Arrays.asList("OPTIONS", "GET", "POST", "DELETE", "HEAD"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public OAuth2RequestFactory requestFactory() {
        CustomOauth2RequestFactory requestFactory = new CustomOauth2RequestFactory(clientDetailsService);
        requestFactory.setCheckUserScopes(false);
        return requestFactory;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint())
                .authenticationEntryPoint(
                        (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and().authorizeRequests().antMatchers("/**").permitAll().and()
                .csrf().disable()
                .formLogin().authenticationDetailsSource(authenticationDetailsSource)
        ;


    }

    @Bean
    public CustomAccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider()).userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
}
