package com.herocheer.zhsq.localservice.core.device.HIKVISON.sdk;

import com.herocheer.zhsq.localservice.core.exception.CheckedException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
@Component
@Data
public class SDKPathSupport {

    private static final Logger logger = LoggerFactory.getLogger(SDKPathSupport.class);

    @Value("${hik.sdk.path}")
    private String hikSdkPath;

    public static String sdkPath(){
        String path;
        try {
            String temp = HCNetSDK.class.getResource("/lib/hik/").getPath()+"HCNetSDK.dll".replaceAll("%20", " ");
                    String path1 = temp.replaceFirst("/","").replace("/","\\\\");
            path = URLDecoder.decode(path1,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new CheckedException("device-0004");
        }
        logger.info("海康SDK路径:{}",path);
        return path;
    }

}
