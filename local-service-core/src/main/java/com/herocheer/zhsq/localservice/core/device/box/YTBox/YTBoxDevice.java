package com.herocheer.zhsq.localservice.core.device.box.YTBox;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.herocheer.zhsq.localservice.core.device.AbstractDevice;
import com.herocheer.zhsq.localservice.core.device.box.YTBox.dict.TypeEnum;
import com.herocheer.zhsq.localservice.core.device.box.YTBox.pojo.CapturePushBean;
import com.herocheer.zhsq.localservice.core.device.box.YTBox.pojo.CapturePushDTO;
import com.herocheer.zhsq.localservice.core.device.box.YTBox.pojo.RetrievalResult;
import com.herocheer.zhsq.localservice.core.device.box.YTBox.pojo.SimilarFace;
import com.herocheer.zhsq.localservice.core.device.entity.*;
import com.herocheer.zhsq.localservice.core.util.*;
import okhttp3.*;
import okio.Buffer;
import okio.BufferedSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Component
public class YTBoxDevice extends AbstractDevice {

    //设备类型
    private static final Integer deviceType = 30;
    //设备厂商标志
    private static final Integer brand = 50;
    //设备支持功能
    private static final Integer deviceSupFun1 = 10; //设备支持的功能 下发人脸
//    @Autowired
    private RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private OkHttpClient httpClient = HttpClientUtils.getOkHttpClient();
    /**
     * token 重建时间
     */
    public static final long tokenPreRebuild = 60 * 60 * 24 * 3;

    @Value("${ytbox.repositoryId}")
    private String repositoryId;
    @Value("${ytbox.catchFacePicPath}")
    private String catchFacePicPath;
    @Value("${ytbox.catchBackgroundPicPath}")
    private String catchBackgroundPicPath;

    private static final Logger logger = LoggerFactory.getLogger(YTBoxDevice.class);

    public YTBoxDevice() {
        super(deviceType, brand,Arrays.asList(deviceSupFun1));
    }

//    @Override
//    public DeviceResponse registerDevice(BaseDevice baseDevice) {
//        String token = stringRedisTemplate.opsForValue().get(baseDevice.getDeviceSn());
//        if(!StringUtils.isEmpty(token)){
//            return new DeviceResponse(true,token);
//    }
//        Map<String, Object>  paramMap = new HashMap<>();
//        paramMap.put("username",baseDevice.getAccount());
//        paramMap.put("password", MD5Util.md5(baseDevice.getPassword()));
//        paramMap.put("not_before", 0);
//        paramMap.put("audience", "web");
//        paramMap.put("subject", "web call api");
//        JSONObject responseJson = request(null, paramMap,String.format(YTBoxApi.TOKEN_URL, baseDevice.getIp(), baseDevice.getPort()),HttpMethod.POST);
//         token = responseJson.getString("token");
//        long tokenStartTime = responseJson.getLong("issue_at");
//        long tokenEndTime = responseJson.getLong("expiration");
//        if(!StringUtils.isEmpty(token)){
//            stringRedisTemplate.opsForValue().set(baseDevice.getDeviceSn(),token,TimeUnit.SECONDS.toSeconds(tokenEndTime-tokenPreRebuild-tokenStartTime));
//            //注册成功启动抓拍
//            captureEvent(baseDevice);
//            return new DeviceResponse(true,token);
//        }
//        return new DeviceResponse(false,responseJson.getString("errMsg"));
//    }

    @Override
    public DeviceResponse doRegisterDevice(BaseDevice baseDevice) {
//        String token = stringRedisTemplate.opsForValue().get(baseDevice.getDeviceSn());
//        if(!StringUtils.isEmpty(token)){
//            return new DeviceResponse(true,token);
//        }
        String token = null;
        Map<String, Object>  paramMap = new HashMap<>();
        paramMap.put("username",baseDevice.getAccount());
        paramMap.put("password", MD5Util.md5(baseDevice.getPassword()));
        paramMap.put("not_before", 0);
        paramMap.put("audience", "web");
        paramMap.put("subject", "web call api");
        JSONObject responseJson = request(null, paramMap,String.format(YTBoxApi.TOKEN_URL, baseDevice.getIp(), baseDevice.getPort()),HttpMethod.POST);
        token = responseJson.getString("token");
//        long tokenStartTime = responseJson.getLong("issue_at");
//        long tokenEndTime = responseJson.getLong("expiration");
        if(!StringUtils.isEmpty(token)){
//            stringRedisTemplate.opsForValue().set(baseDevice.getDeviceSn(),token,TimeUnit.SECONDS.toSeconds(tokenEndTime-tokenPreRebuild-tokenStartTime));
            return new DeviceResponse(true,token);
        }
        return new DeviceResponse(false,responseJson.getString("errMsg"));
    }

    @Override
    public DeviceResponse setFace(BaseDevice baseDevice, BaseFace baseFace) {
        Map<String,Object> params = new HashMap<>();
        JSONObject params2 = new JSONObject();
        params2.put("content_type","jpeg");
        params2.put("content",ImageUtil.imageToBase64(new File(baseFace.getJpgFilePath())));
        params.put("face_image", params2);
        YTBoxFace ytBoxFace = new YTBoxFace(baseFace.getUserId(),null,null,baseFace.getName());
        params.put("extra_meta", JSONObject.toJSONString(ytBoxFace));
        JSONObject rep = request(doRegisterDevice(baseDevice).getMessage(), params, String.format(YTBoxApi.SET_ONE_FACE, baseDevice.getIp(), baseDevice.getPort(), repositoryId, baseFace.getUserId()), HttpMethod.PUT);
       if(null==rep){
           logger.info("YT智脑盒子下发人脸成功");
           return new DeviceResponse(true);
       }
        return new DeviceResponse(false,rep.getString("errMsg"));
    }

    @Override
    public DeviceResponse updateFace(BaseDevice baseDevice, BaseFace baseFace) {
        DeviceResponse deviceResponse = delFace(baseDevice, baseFace);
        if (deviceResponse.getIsSuccess()) {
            return setFace(baseDevice, baseFace);
        } else {
            return deviceResponse;
        }
    }

    @Override
    public DeviceResponse delFace(BaseDevice baseDevice, BaseFace baseFace) {
        JSONObject rep = request(doRegisterDevice(baseDevice).getMessage(), null, String.format(YTBoxApi.DEL_ONE_FACE, baseDevice.getIp(), baseDevice.getPort(), repositoryId, baseFace.getUserId()), HttpMethod.DELETE);
        if(null==rep){
            return new DeviceResponse(true);
        }
        return new DeviceResponse(false,rep.getString("errMsg"));
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

//    @Override
//    public Integer getDeviceType() {
//        return this.deviceType;
//    }
//
//    @Override
//    public List<Integer> getDeviceSupFun() {
//        List<Integer> typeList = new ArrayList<>();
//        typeList.add(deviceSupFun1);
//        return typeList;
//    }

    @Override
    public Boolean isSupportSetFace() {
       return true;
    }

    @Override
    public Boolean isSupportSetCard() {
//        throw new CheckedException("device-0006");
        return false;
    }

    @Override
    public Boolean isSupportCapture() {
      return true;
    }

    @Override
    public void captureEvent(BaseDevice baseDevice) {
        doCaptureEvent(baseDevice,this.faceResultEventHandler);
    }

    public void doCaptureEvent(BaseDevice baseDevice, IEventManager.IFaceResultEventHandler faceResultEventHandler) {
        logger.info("===============启动YT盒子监听=================");
        Request request = new Request.Builder()
                .url(String.format(YTBoxApi.FACE_RESULT_PATH, baseDevice.getIp(), YTBoxApi.STREAMING_CLIENT_PORT))
                .build();
        httpClient.newCall(request).enqueue(new Callback() {

            private FaceResultExecutor FaceResultExecutor;

            @Override
            public void onFailure(Call call, IOException e) {
                logger.error("{} connection failed!", baseDevice.getIp());
                if (FaceResultExecutor != null) {
                    FaceResultExecutor.terminate(HttpEventType.FACE_RESULT);
                }
                //重连
                captureEvent(baseDevice);

            }
            /**
             * 处理HTTP2 Response的回调
             * @param call
             * @param response response对象
             */
            @Override
            public void onResponse(Call call, Response response) {
                logger.info("onResponse headers: {}", response.headers());
                if(null==FaceResultExecutor){
                    FaceResultExecutor = new FaceResultExecutor(faceResultEventHandler, baseDevice, response.header(YTBoxApi.SCHEMA_HEADER), httpClient);
                }
               try {
                    /**
                     * 逐帧读取Response内容，放入处理队列
                     *
                     */
                    while (response.body() != null && !response.body().source().exhausted()) {
                        Buffer buffer = new Buffer();
                        ResponseBody responseBody = response.body();
                        BufferedSource bufferedSource = responseBody.source();
                        bufferedSource.read(buffer, 8192);
                        try {
                            FaceResultExecutor.processData(buffer);
                        } catch (Exception e) {
                            logger.error("processCaptureResult failed failed: {}", e.getMessage());
                        }
                    }
                } catch (SocketTimeoutException ignored) {
                    logger.info("SocketTimeoutException: {}", ignored.getMessage());
                } catch (IOException e) {
                    logger.error("PingPong timeout: %s", e.getMessage());
                    /**
                     * 如果连接超时，停止数据处理
                     */
                    if (response.body() != null) {
                        response.body().close();
                    }
                    if (FaceResultExecutor != null) {
                        FaceResultExecutor.terminate(HttpEventType.FACE_RESULT);
                    }
                    httpClient.dispatcher().executorService().shutdown();
                    httpClient = HttpClientUtils.getOkHttpClient();
                    //重连
                    captureEvent(baseDevice);
                }
            }
        });
    }

    /**
     * 处理人像结果回调
     */
    private IEventManager.IFaceResultEventHandler faceResultEventHandler = (retrivalFaceResult,baseDevice) -> {
        logger.info("====================进入盒子回调=======================");
        // 人脸照
        byte[] faceImageContent = retrivalFaceResult.getFace().getFaceImage().getContent().array();
        // 全景图
        byte[] sceneContent = retrivalFaceResult.getFace().getSceneImage().getContent().array();
        String extraMetaJson = retrivalFaceResult.getFace().getExtraMeta();
        logger.info(extraMetaJson);
        if (StringUtils.isEmpty(extraMetaJson)) {
            return;
        }
        JSONObject extraMetaObj = JSONObject.parseObject(extraMetaJson);

        //获取照片信息 年龄等
        CapturePushDTO capturePushVO = new CapturePushDTO();
        Map<String, Object> enumMap = new HashMap<>();
        enumMap.put("deviceType",deviceType);
        enumMap.put("deviceIp",baseDevice.getIp());
        enumMap.put("devicePassword",baseDevice.getPassword());
        enumMap.put("deviceAccount",baseDevice.getAccount());
        enumMap.put("devicePort",baseDevice.getPort());
        enumMap.put("deviceSn",baseDevice.getDeviceSn());
        JSONArray visionAttributes = extraMetaObj.getJSONArray("vision_attributes");
        logger.warn(visionAttributes.toJSONString());
        for (int i = 0; i < visionAttributes.size(); i++) {
            JSONObject vao = visionAttributes.getJSONObject(i);
            String type = vao.getString("type_");
            String typeValue = vao.getString("value");
            Double confidence = vao.getDouble("confidence");
//            int typeValueYesOrNo = "NO".equals(typeValue) ? 0 : 1;

            /**
             * 转换成对应推送值
             */
            switch (type) {
                case "GENDER":
                    enumMap.put("sex", com.herocheer.ytbox.demo.dict.GenderEnum.getSex(typeValue));
                    enumMap.put("sexConfidence", confidence);
                    break;
                case "AGE":
                    enumMap.put("ageRange", TypeEnum.getTypeName(vao.getString("value")));
                    break;
            }
        }

        //保存图片
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date capTime = new Date();
        String datetime = sdf.format(capTime);
        String fileName;
        //按日期创建文件夹
        String preFacePath = FileUtil.makeDynmicsDateDir(catchFacePicPath);
        String preBackPath = FileUtil.makeDynmicsDateDir(catchBackgroundPicPath);
        /**
         * 比对结果的库id与前k个相似人脸
         */
        List<RetrievalResult> retrievalResults = retrivalFaceResult.getRetrievalResults();
        //判断是否有相似人脸
        if (CollectionUtils.isEmpty(retrievalResults.stream().filter(x -> !x.getSimilarFaces().isEmpty()).collect(Collectors.toList()))) {
            logger.info("无匹配人脸>>>>陌生人");
            fileName = "MSR_"+datetime+".jpg";
            //保存图片
            ImageUtil.byteToImage(faceImageContent, preFacePath + fileName);
            ImageUtil.byteToImage(sceneContent, preBackPath + fileName);
            // 直接返回
            // TODO 优化
            BaseEvent baseEvent = new BaseEvent(this,null,0,new Date(),preFacePath + fileName,preBackPath + fileName,null,null,
                    null,extraMetaObj.getString("channel_name"),null,null,null, enumMap,null);
            SpringUtil.getApplicationContext().publishEvent(baseEvent);
            return;
        }

        List<CapturePushBean> compareInfos = new ArrayList<>();
        for (RetrievalResult r : retrievalResults) {
            /**
             * 前k个相似人脸
             */
            List<SimilarFace> similarFaces = r.getSimilarFaces();
            for (SimilarFace similarFace : similarFaces) {
                long similarity = Math.round(similarFace.getSimilarity());
                logger.info("匹配相似度: {}", similarity);
                if (similarity > 70) {
                    JSONObject similarFaceExtraMetaObj = JSONObject.parseObject(similarFace.getExtraMeta());
                    logger.info("人像匹配数据: {}", similarFaceExtraMetaObj.toJSONString());
                    CapturePushBean compareInfo = new CapturePushBean();
                    compareInfo.setRepositoryId(r.getRepositoryId());
                    compareInfo.setUserId(similarFaceExtraMetaObj.getString("id"));
                    compareInfo.setUserName(similarFaceExtraMetaObj.getString("name"));
                    compareInfo.setUserType(1);
                    compareInfo.setSimilarity(similarity);
                    compareInfos.add(compareInfo);
                }
            }
        }

        // 筛选出比对值最高的
        CapturePushBean compareInfo = compareInfos.stream().max((o1, o2) -> o1.getSimilarity() > o2.getSimilarity() ? 1 : -1).orElse(null);
        if (compareInfo != null) {
            capturePushVO.setUser(compareInfo.getUserId());
            capturePushVO.setUser_info(compareInfo.getUserName());
            capturePushVO.setPtype(compareInfo.getUserType());
            capturePushVO.setMatch(compareInfo.getSimilarity().floatValue() / 100);
            capturePushVO.setSex(compareInfo.getSex());
            fileName = "ID_" + compareInfo.getUserId() + "_" + datetime + ".jpg";
            //写入指定文件
            ImageUtil.byteToImage(faceImageContent, preFacePath + fileName);
            ImageUtil.byteToImage(sceneContent, preBackPath + fileName);
            //capturePushVO.setAge(compareInfo.getMinAge());
            // TODO 优化
            BaseEvent baseEvent = new BaseEvent(this,Integer.parseInt(compareInfo.getUserId()),0,new Date(),preFacePath + fileName,preBackPath + fileName,null,null,
                    null,extraMetaObj.getString("channel_name"),String.valueOf(capturePushVO.getMatch()*100),null,null, enumMap,null);
            SpringUtil.getApplicationContext().publishEvent(baseEvent);
        }




    };


    @Override
    public DeviceResponse isOnline(BaseDevice baseDevice) {

        JSONObject rep = request(doRegisterDevice(baseDevice).getMessage(), null, String.format(YTBoxApi.GET_BOX_ID, baseDevice.getIp(), baseDevice.getPort()),HttpMethod.GET);
        if(baseDevice.getDeviceSn().equals(rep.getString("id"))){
            logger.info("YT盒子在线情况：在线");
            return new DeviceResponse(true);
        }
        logger.info("YT盒子在线情况：离线");
        return new DeviceResponse(false,rep.getString("errMsg"));
    }


    @Override
    public DeviceResponse openGuardOrder(BaseDevice baseDevice) {
        return null;
    }

    public JSONObject request(String token, Map<String, Object> params,String url,HttpMethod httpMethod){
        Assert.hasText(url,"url is null");
        HttpHeaders requestHeaders = new HttpHeaders();
        if(!StringUtils.isEmpty(token)){
            requestHeaders.add("token",token);
        }
        requestHeaders.add("Content-Type","application/json");
        requestHeaders.add("Accept","application/json");
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params,requestHeaders);
        JSONObject response = null;
        try {
            response = restTemplate.exchange(url, httpMethod, requestEntity, JSONObject.class).getBody();
        }catch (Exception e){
            logger.error("调用接口异常，接口名:{},异常：{}",url,e.getMessage());
            JSONObject repJson = new JSONObject();
            repJson.put("errMsg",e.getMessage());
            return repJson;
        }
        return  response;
    }
}
