package com.github.charlesvhe.springcloud.practice.consumer.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "PROVIDER")
public interface UserService {
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String query();
}
