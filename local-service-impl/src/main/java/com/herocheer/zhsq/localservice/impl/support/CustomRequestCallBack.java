package com.herocheer.zhsq.localservice.impl.support;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

public class CustomRequestCallBack implements RequestCallback {
    private RestTemplate restTemplate;
    private List<HttpMessageConverter<?>> messageConverters;
    private HttpEntity requestEntity;
    public CustomRequestCallBack(RestTemplate restTemplate,HttpEntity requestEntity){
        this.restTemplate = restTemplate;
        this.messageConverters = restTemplate.getMessageConverters();
        this.requestEntity = requestEntity;
    }

    @Override
    public void doWithRequest(ClientHttpRequest clientHttpRequest) throws IOException {
        if (!requestEntity.hasBody()) {
            HttpHeaders httpHeaders = clientHttpRequest.getHeaders();
            HttpHeaders requestHeaders = requestEntity.getHeaders();
            if (!requestHeaders.isEmpty()) {
                httpHeaders.putAll(requestHeaders);
            }
            if (httpHeaders.getContentLength() < 0L) {
                httpHeaders.setContentLength(0L);
            }

        }else {
            Object requestBody = requestEntity.getBody();
            Iterator iterator = messageConverters.iterator();
            while (iterator.hasNext()) {
                Class<?> requestBodyClass = requestBody.getClass();
                Type requestBodyType = requestEntity instanceof RequestEntity ? ((RequestEntity) requestEntity).getType() : requestBodyClass;
                HttpHeaders requestHeadersx = requestEntity.getHeaders();
                MediaType requestContentType = requestHeadersx.getContentType();
                HttpMessageConverter<Object> messageConverter = (HttpMessageConverter) iterator.next();
                if (messageConverter instanceof GenericHttpMessageConverter) {
                    GenericHttpMessageConverter<Object> genericMessageConverter = (GenericHttpMessageConverter) messageConverter;
                    if (genericMessageConverter.canWrite((Type) requestBodyType, requestBodyClass, requestContentType)) {
                        if (!requestHeadersx.isEmpty()) {
                            clientHttpRequest.getHeaders().putAll(requestHeadersx);
                        }
                        genericMessageConverter.write(requestBody, (Type) requestBodyType, requestContentType, clientHttpRequest);
                        return;
                    }
                } else if (messageConverter.canWrite(requestBodyClass, requestContentType)) {
                    if (!requestHeadersx.isEmpty()) {
                        clientHttpRequest.getHeaders().putAll(requestHeadersx);
                    }
                    messageConverter.write(requestBody, requestContentType, clientHttpRequest);
                    return;
                }
            }
        }
    }
}
