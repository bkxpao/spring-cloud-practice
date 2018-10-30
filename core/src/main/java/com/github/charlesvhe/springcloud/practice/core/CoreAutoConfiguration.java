package com.github.charlesvhe.springcloud.practice.core;

import feign.RequestInterceptor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by charles on 2017/5/25.
 */
@Configuration
@EnableWebMvc
public class CoreAutoConfiguration extends WebMvcConfigurerAdapter {

//    @Bean
//    public DefaultPropertiesFactory defaultPropertiesFactory() {
//        return new DefaultPropertiesFactory();
//    }
//
//    @LoadBalanced
//    @Bean
//    public RestTemplate restTemplate() {
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.getInterceptors().add(new CoreHttpRequestInterceptor());
//        return restTemplate;
//    }
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new CoreHttpRequestInterceptor();
    }

    @Bean LabelAndWeightMetadataRule rule() {
        return new LabelAndWeightMetadataRule();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CoreHeaderInterceptor());
    }
}
