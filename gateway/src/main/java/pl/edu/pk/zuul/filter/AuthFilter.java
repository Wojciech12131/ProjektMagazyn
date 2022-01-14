package pl.edu.pk.zuul.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class AuthFilter implements GlobalFilter, Ordered {

    final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (exchange.getRequest().getPath().pathWithinApplication().value().equals("/oauth/token")) {
            ServerHttpRequest.Builder mutableRequest = exchange.getRequest().mutate();

            if (!exchange.getRequest().getHeaders().containsKey("Authorization"))
                mutableRequest.header(HttpHeaders.AUTHORIZATION, "Basic " +
                        Base64.getEncoder().encodeToString(("client:" + "client").getBytes(StandardCharsets.UTF_8)));

            if (exchange.getRequest().getQueryParams().containsKey("grant_type"))
                return chain.filter(exchange.mutate().request(mutableRequest.build()).build());
            mutableRequest = mutableRequest
                    .uri(UriComponentsBuilder.fromUri(exchange.getRequest().getURI()).replaceQueryParam("grant_type", "password").build().toUri());
            return chain.filter(exchange.mutate().request(mutableRequest.build()).build());
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
