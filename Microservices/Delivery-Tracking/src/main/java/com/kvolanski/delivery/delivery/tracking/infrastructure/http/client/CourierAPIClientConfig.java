package com.kvolanski.delivery.delivery.tracking.infrastructure.http.client;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

@Configuration
public class CourierAPIClientConfig {

    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalancendRestClientBuilder() {
        return RestClient.builder();
    }
    
    @Bean
    public CourierAPIClient courierAPIClient(RestClient.Builder builder){
        RestClient restClient = builder.baseUrl("http://courier-management")
                .requestFactory(generateClientRequestFactory())
                .build();
        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return proxyFactory.createClient(CourierAPIClient.class);
    }

    private ClientHttpRequestFactory generateClientRequestFactory(){
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(Duration.ofMillis(10));
        simpleClientHttpRequestFactory.setReadTimeout(Duration.ofMillis(200));
        return simpleClientHttpRequestFactory;
    }

}



