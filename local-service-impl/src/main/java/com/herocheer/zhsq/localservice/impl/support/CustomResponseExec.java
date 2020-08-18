package com.herocheer.zhsq.localservice.impl.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.herocheer.zhsq.localservice.core.base.RestResult;
import com.herocheer.zhsq.localservice.core.util.JSDesUtils;
import com.herocheer.zhsq.localservice.core.util.LSStringUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Type;

public class CustomResponseExec<T> implements ResponseExtractor<ResponseEntity<T>> {

    private HttpMessageConverterExtractor<String> delegate;
    private String key;
    private Class<T> type;

    public CustomResponseExec(RestTemplate restTemplate){
        Type responseType = String.class;
        if (responseType != null && Void.class != responseType) {
            this.delegate = new HttpMessageConverterExtractor(responseType, restTemplate.getMessageConverters());
        } else {
            this.delegate = null;
        }
    }

    public CustomResponseExec(RestTemplate restTemplate,String key,Class<T> responseType){
        this.key = key;
        this.type = responseType;
        Type rt = String.class;
        if (rt != null && Void.class != rt) {
            this.delegate = new HttpMessageConverterExtractor(rt, restTemplate.getMessageConverters());
        } else {
            this.delegate = null;
        }
    }

    @Override
    public ResponseEntity<T> extractData(ClientHttpResponse response) throws IOException {
        if (this.delegate != null) {
            Object body = this.delegate.extractData(response);
            if(StringUtils.isNotEmpty(key)){
                try {
                    //解密 将前后多余的分号去除
                    String enCodeStr = LSStringUtil.trimFirstAndLastChar(String.valueOf(body), "\"");
                    body = JSDesUtils.decode(enCodeStr,key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            RestResult restResult = JSON.parseObject(String.valueOf(body), RestResult.class);
            if(type != null && !type.getName().equalsIgnoreCase("java.lang.String")){
                body = new ObjectMapper().readValue(JSONObject.toJSONString(restResult.getData()), type);
            }
            return new ResponseEntity(body, response.getHeaders(), response.getStatusCode());
        } else {
            return new ResponseEntity(response.getHeaders(), response.getStatusCode());
        }
    }


}
