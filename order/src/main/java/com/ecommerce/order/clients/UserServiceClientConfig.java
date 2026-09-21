package com.ecommerce.order.clients;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Optional;


@Configuration
public class UserServiceClientConfig {

    @Autowired(required = false)
    private ObservationRegistry observationRegistry;

    // Load-balanced builder — only used by your own service client.
    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalancedRestClientBuilderUser() {
        return RestClient.builder().observationRegistry(observationRegistry);
    }

    @Bean
    public UserServiceClient getUserServiceClient(
            @Qualifier("loadBalancedRestClientBuilderUser") RestClient.Builder builder) {

        RestClient restClient = builder
                .baseUrl("http://USER-SERVICE")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError,
                        (request, response) -> Optional.empty())
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(UserServiceClient.class);
    }
}

