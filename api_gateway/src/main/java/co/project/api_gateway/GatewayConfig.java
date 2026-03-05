package co.project.api_gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("auth-service", r -> r
                .path("/auth/**")
                .uri("http://api-auth:8082"))
            .route("pedidos-service", r -> r
                .path("/api/pedidos/**")
                .uri("http://api-pedidos:8083"))
            .route("catalogo-service", r -> r
                .path("/api/catalogo/**")
                .uri("http://api-catalogo:8081"))
            .build();
    }
}