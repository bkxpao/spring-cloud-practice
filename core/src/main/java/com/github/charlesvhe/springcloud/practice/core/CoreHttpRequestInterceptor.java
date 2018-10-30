package com.github.charlesvhe.springcloud.practice.core;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * Created by charles on 2017/5/26.
 */
public class CoreHttpRequestInterceptor implements RequestInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(CoreHttpRequestInterceptor.class);

    @Override
    public void apply(RequestTemplate template) {
        System.out.println("**进入路由规则判断**");
        if (!StringUtils.isEmpty(template.request().headers().get(CoreHeaderInterceptor.HEADER_LABEL))) {
            System.out.println("**进行路由规则**");
        }

        String header = CoreHeaderInterceptor.label.get();
        logger.info("塞入Header label: " + header);
        template.header(CoreHeaderInterceptor.HEADER_LABEL, header);
    }
}
