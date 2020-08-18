package com.herocheer.zhsq.localservice.core.device.analysis;


import com.herocheer.zhsq.localservice.core.base.RestResult;
import com.herocheer.zhsq.localservice.core.base.enums.EventTypeEnum;
import com.herocheer.zhsq.localservice.core.device.AbstractDevice;
import com.herocheer.zhsq.localservice.core.device.entity.*;
import com.herocheer.zhsq.localservice.core.util.*;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.*;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AnalysisDevice extends AbstractDevice {
    private static CloseableHttpClient httpClient;
    static {
        httpClient = getHttpClient();
    }
    //设备类型
    private static final Integer deviceType = 40;
    //设备厂商标志
    private static final Integer brand = 40;
    //设备支持功能
    private static final Integer deviceSupFun1 = 10; //设备支持的功能 下发人脸
    @Value("${analysis.picPath}")
    private String picPath;
    @Value("${analysis.linuxPath}")
    private String linuxPath;

    @Value("${analysis.videoPath}")
    private String videoPath;

    private Boolean httpIsOpen = true;

    private static final Logger logger = LoggerFactory.getLogger(AnalysisDevice.class);

    private Map<String,Boolean> deviceCapIsStart = new ConcurrentHashMap<>(16);

    public AnalysisDevice() {
        super(deviceType, brand, Arrays.asList(deviceSupFun1));
    }

    public static CloseableHttpClient getHttpClient(){
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(50);
         return HttpClients.custom().setConnectionManager(cm).setRetryHandler(retryHandler(3)).build();
    }

    @Override
    public DeviceResponse doRegisterDevice(BaseDevice baseDevice) {
        return this.isOnline(baseDevice);
    }

    @Override
    public DeviceResponse setFace(BaseDevice baseDevice, BaseFace baseFace) {
        return null;
    }

    @Override
    public DeviceResponse updateFace(BaseDevice baseDevice, BaseFace baseFace) {
        return null;
    }

    @Override
    public DeviceResponse delFace(BaseDevice baseDevice, BaseFace baseFace) {
        return null;
    }

    @Override
    public DeviceResponse setCard(BaseDevice baseDevice, BaseCard baseCard) {
        return null;
    }

    @Override
    public DeviceResponse updateCard(BaseDevice baseDevice, BaseCard baseCard) {
        return null;
    }

    @Override
    public DeviceResponse delCard(BaseDevice baseDevice, BaseCard baseCard) {
        return null;
    }


    @Override
    public Boolean isSupportSetFace() {
        return false;
    }

    @Override
    public Boolean isSupportSetCard() {
        return false;
    }

    @Override
    public Boolean isSupportCapture() {
         return true;
    }

    @Override
    public void captureEvent(BaseDevice baseDevice) {
//        if(deviceCapIsStart.containsKey(baseDevice.getDeviceSn())&&deviceCapIsStart.get(baseDevice.getDeviceSn())){
//            return;
//        }
        new Thread(() -> {
            CloseableHttpResponse response = null;
            String url="http://" + baseDevice.getIp() + ":" + baseDevice.getPort() + "/api/v1/events/notifications";
            try {
                // 创建uri
                URIBuilder builder = new URIBuilder(url);
                URI uri = builder.build();
                // 创建http GET请求
                HttpGet httpGet = new HttpGet(uri);
                httpGet.setHeader("Connection","Keep-Alive");
                // 执行请求
                if(!httpIsOpen){
                    httpClient = getHttpClient();
                }
                response = httpClient.execute(httpGet);
                // 判断返回状态是否为200
                InputStream inputStream = null;
                if (response.getEntity()!= null) {
                    logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>进入行为分析仪回调");
                    deviceCapIsStart.put(baseDevice.getDeviceSn(),true);
                    inputStream = response.getEntity().getContent();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    String data = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        if(line.contains("data:")){
                            data=line.substring(6);
                            BehaviorAnalysisData eventInfo=new BehaviorAnalysisData();
                            Map map= JSONUtils.jsonToMap(data);
                            if(map.get("videoPath")!=null&&map.get("videoPath")!=""){
                                if(map.get("snapPath")!=null&&map.get("snapPath")!=""){
                                    String snapUrl= FileUtil.makeDynmicsDateDir(picPath)+map.get("id").toString().replace("-","")+".jpg";
                                    Boolean downloadResult= DownloadUtils.httpDownload("http://" + baseDevice.getIp() + ":" + baseDevice.getPort()
                                            + map.get("snapPath"),snapUrl);
                                    eventInfo.setPicUrl(snapUrl);
                                }
                                String videoNm = map.get("id").toString().replace("-","")+".mp4";
                                String filePrx = FileUtil.makeDynmicsDateDir(videoPath);
                                String videoUrl = filePrx+videoNm;
                                Boolean downloadResult=DownloadUtils.httpDownload("http://" + baseDevice.getIp() + ":" + baseDevice.getPort()+ map.get("videoPath"),
                                        videoUrl);
                                //压缩视频
                                String compressVideoUrl = filePrx+"c_"+videoNm;
                                VideoUtil.compressVideo(videoUrl,compressVideoUrl);
                                eventInfo.setVideoUrl(compressVideoUrl);
                                if(map.get("time")!=null){
                                    eventInfo.setAlarmTime(new Date((Long) map.get("time")));
                                }
                                if(map.get("types")!=null){
                                    String changeType=map.get("types").toString().replace(" ","");
                                    String[] type=changeType.substring(1,changeType.length()-1).split(",");
                                    String EventTypes="";
                                    for (int i=0;i<type.length;i++){
                                        if(type[i].length()>0){
                                            EventTypeEnum eventType=EventTypeEnum.valueOf(type[i]);
                                            if(i!=0){
                                                EventTypes+="|";
                                            }
                                            EventTypes+=eventType.getValue();
                                        }
                                    }
                                    eventInfo.setEventName(EventTypes);
                                }
                                BaseEvent baseEvent =  new BaseEvent(this,null,2,eventInfo.getAlarmTime(),
                                        eventInfo.getPicUrl(),null, null,null,baseDevice.getDeviceSn(),null,null,eventInfo.getEventName(),eventInfo.getVideoUrl());
                                SpringUtil.getApplicationContext().publishEvent(baseEvent);
                            }
                        }
                    }
                }
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                deviceCapIsStart.put(baseDevice.getDeviceSn(),false);
                httpIsOpen = false;
                this.captureEvent(baseDevice);
            } finally {
                try {
                    if (response != null) {
                        response.close();
                    }
                    httpClient.close();
                    httpIsOpen = false;
                } catch (IOException e) {
                }
            }}).start();

    }

    @Override
    public DeviceResponse isOnline(BaseDevice baseDevice) {
        RestResult restResult = this.doGet("http://" + baseDevice.getIp() + ":" + baseDevice.getPort() + "/api/v1/events/cameras", null);
        logger.info("设备序列号：{}，在线情况：{}",baseDevice.getDeviceSn(), restResult.getSuccess()?"在线":"离线");
        return new DeviceResponse(restResult.getSuccess(), "返回码：" + restResult.getCode() + "返回信息:" + restResult.getMsg());
    }

    @Override
    public DeviceResponse openGuardOrder(BaseDevice baseDevice) {
        return null;
    }
    public static HttpRequestRetryHandler retryHandler(final int tryTimes){

        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                // 如果已经重试了n次，就放弃
                if (executionCount >= tryTimes) {
                    return false;
                }
                // 如果服务器丢掉了连接，那么就重试
                if (exception instanceof NoHttpResponseException) {
                    return true;
                }
                // 不要重试SSL握手异常
                if (exception instanceof SSLHandshakeException) {
                    return false;
                }
                // 超时
                if (exception instanceof InterruptedIOException) {
                    return false;
                }
                // 目标服务器不可达
                if (exception instanceof UnknownHostException) {
                    return true;
                }
                // SSL握手异常
                if (exception instanceof SSLException) {
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };
        return httpRequestRetryHandler;
    }
    public   RestResult doGet(String url, Map<String, String> param) {
        CloseableHttpResponse response = null;
        RestResult restResult;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            // 执行请求
            if(!httpIsOpen){
                httpClient = getHttpClient();
            }
            response = httpClient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                restResult= RestResult.success();
            }else{
                restResult= RestResult.build(false,"请求失败");
            }
            restResult.setCode(response.getStatusLine().getStatusCode());
        } catch (Exception e) {
            restResult= RestResult.fail(e.toString());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
                httpIsOpen = false;
            } catch (IOException e) {
                restResult= RestResult.fail(e.toString());
            }
        }
        return restResult;
    }
}
