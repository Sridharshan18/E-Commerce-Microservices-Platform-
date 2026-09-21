package com.ecommerce.order.clients;


import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Optional;

@Configuration
public class ProductServiceClientConfig {

    @Autowired(required = false)
    private ObservationRegistry observationRegistry;

    // Load-balanced builder — only used by your own service client.
    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalancedRestClientBuilderProduct() {
        return RestClient.builder().observationRegistry(observationRegistry);
    }

    @Bean
    public ProductServiceClient getProductServiceClient(
            @Qualifier("loadBalancedRestClientBuilderProduct") RestClient.Builder builder) {

        RestClient restClient = builder
                .baseUrl("http://PRODUCT-SERVICE")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError,
                        (request, response) -> Optional.empty())
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(ProductServiceClient.class);
    }
}
