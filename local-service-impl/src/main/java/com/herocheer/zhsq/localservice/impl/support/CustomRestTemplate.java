package com.herocheer.zhsq.localservice.impl.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.herocheer.zhsq.localservice.core.base.RequestVO;
import com.herocheer.zhsq.localservice.core.exception.CheckedException;
import com.herocheer.zhsq.localservice.core.util.JSDesUtils;
import com.herocheer.zhsq.localservice.impl.config.CommonProperties;
import com.herocheer.zhsq.localservice.impl.domain.constant.AESKey;
import com.herocheer.zhsq.localservice.impl.domain.entity.TokenInfo;
import io.netty.handler.timeout.ReadTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 自定义请求类
 */
@Component("customRestTemplate")
public class CustomRestTemplate   {


    @Autowired
    private CommonProperties commonProperties;

    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(CustomRestTemplate.class);

    public CustomRestTemplate(){
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(1000 * 3);
        httpRequestFactory.setConnectTimeout(1000 * 3);
        httpRequestFactory.setReadTimeout(1000 * 3);
        restTemplate = new RestTemplate(httpRequestFactory);
    }


    public <T> ResponseEntity<T> execute(String url, HttpMethod method, Map<String, Object> requestParams, Class<T> responseType, Boolean isToken, List<String> imgs) throws RestClientException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/json");
        if(isToken){
            headers.set("Token",getToken().getTokenValue());
        }
        String xContent = JSON.toJSONString(requestParams);
        String content = JSDesUtils.encode(xContent,AESKey.secretKey);
        List<String> encodeImg = null;
        RequestVO requestVO = new RequestVO();
        if(null!=imgs&&!imgs.isEmpty()&&imgs.size()>0){
            encodeImg = imgs.stream().map(eaStr -> JSDesUtils.fileEncode(eaStr, AESKey.fileDeCodeSecretKey)).collect(Collectors.toList());
            requestVO.setImgs(encodeImg);
        }
        requestVO.setData(content);
        HttpEntity<?> requestEntity = new HttpEntity(requestVO,headers);
        RequestCallback requestCallback = new CustomRequestCallBack(restTemplate,requestEntity);
        ResponseExtractor<T> responseExtractor = new CustomResponseExec(restTemplate, AESKey.secretKey,responseType);
        ResponseEntity responseEntity = null;
          try {
              responseEntity = (ResponseEntity)restTemplate.execute(url, method, requestCallback, responseExtractor);
          }catch (ResourceAccessException e) {
              logger.error("读取超时，限制视频传输",url,e.getMessage());
              requestVO.setImgs(null);
              requestEntity = new HttpEntity(requestVO,headers);
              requestCallback = new CustomRequestCallBack(restTemplate,requestEntity);
              try {
                  restTemplate.execute(url, method, requestCallback, responseExtractor);
              }catch (Exception ex){
                  logger.error("调用接口异常，接口名:{},异常：{}",url,ex.getMessage());
              }
          }catch (Exception e){
              logger.error("调用接口异常，接口名:{},异常：{}",url,e.getMessage());
//              throw new CheckedException("sys-0005",url,e.getMessage());
          }
        return responseEntity;
    }
    public <T> ResponseEntity<T> execute(String url, HttpMethod method, Map<String, Object> requestParams, Class<T> responseType) throws RestClientException {
        return execute(url, method, requestParams,  responseType,true,null);
    }
    public <T> ResponseEntity<T> execute(String url, HttpMethod method, Map<String, Object> requestParams, Class<T> responseType,List<String> imgs) throws RestClientException {
        return execute(url, method, requestParams,  responseType,true,imgs);
    }
    public <T> ResponseEntity<T> execute(String url, HttpMethod method, Object requestObject, Class<T> responseType) throws RestClientException {
        Map<String, Object> stringObjectMap = BeanUtils.beanToMap(requestObject);
        return execute(url, method, stringObjectMap,  responseType,true,null);
    }
    public <T> ResponseEntity<T> execute(String url, HttpMethod method, Object requestObject, Class<T> responseType,List<String> imgs) throws RestClientException {
        Map<String, Object> stringObjectMap = BeanUtils.beanToMap(requestObject);
        return execute(url, method, stringObjectMap,  responseType,true,imgs);
    }

    public TokenInfo getToken(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/json");
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("loginName",commonProperties.getLoginName());
        userMap.put("password",commonProperties.getPassword());
        ResponseEntity<TokenInfo> responseEntity = execute(commonProperties.getTokenApi(), HttpMethod.POST, userMap, TokenInfo.class, false,null);
        if(null==responseEntity||null==responseEntity.getBody()){
            throw new CheckedException("sys-0003");
        }
        return responseEntity.getBody();

    }

}
