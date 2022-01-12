package pl.edu.pk.zuul.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutingConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/oauth/**")
                        .uri("lb://auth-service/oauth/"))
                .route(r -> r.path("/mag/**")
                        .uri("lb://mag-service"))
                .build();
    }
}
