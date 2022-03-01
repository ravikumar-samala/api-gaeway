package com.test.microservice.apigateway;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class ApiGateWayConfiguration {

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder){


        return builder.routes()
                .route(p -> p.path("/get")
                        .filters(f -> f.addRequestHeader("MyUri","Value")
                        .addRequestParameter("MyParam","value"))
                        .uri("http://httpbin.org:80"))
                .route(p -> p.path("/currency-exchange/**")   //any request with currency exchange uri will find
                                                                        //from eureka and loadbalance
                        .uri("lb://currency-exchange"))
                .route(k -> k.path("/currency-conversion/**")
                .uri("lb://currency-conversion"))
                .route(k -> k.path("/currency-conversion-feign/**")
                .uri("lb://currency-conversion"))
                .route(k -> k.path("/currency-conversion-new/**")
                        .filters(p -> p.rewritePath("/currency-conversion-new/(?<segment>.*)",
                                "/currency-conversion-feign/${segment}"))
                .uri("lb://currency-conversion"))
                .build()
                ;
    }
}
