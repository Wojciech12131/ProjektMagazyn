package pl.edu.pk.auth.config;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import pl.edu.pk.auth.model.User;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomTokenEnhancer extends JwtAccessTokenConverter {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        if (null != authentication.getUserAuthentication()) {
            User user = (User) authentication.getPrincipal();

            Map<String, Object> info = new LinkedHashMap<>(accessToken.getAdditionalInformation());

            info.put("email", user.getEmail());

            DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
            customAccessToken.setAdditionalInformation(info);

            return super.enhance(customAccessToken, authentication);
        }
        return super.enhance(accessToken, authentication);
    }
}