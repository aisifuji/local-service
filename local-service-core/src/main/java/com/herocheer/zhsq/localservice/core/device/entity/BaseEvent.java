package com.herocheer.zhsq.localservice.core.device.entity;

import org.springframework.context.ApplicationEvent;

import java.util.Date;
import java.util.Map;

public class BaseEvent extends ApplicationEvent {

    private Integer userId;

    private Integer eventType;

    private Date catchTime;

    private String facePicUrl;

    private String backgroundPicUrl;

    private String videoUrl;

    private String openType;

    private Integer openFlg;

    private String deviceSn;

    private String faceMatchDegree;

    private String cardNo;

    private String analysisType;

    private Map<String,Object> msgMap;

    private BaseDevice baseDevice;

    public Map<String, Object> getMsgMap() {
        return msgMap;
    }

    public void setMsgMap(Map<String, Object> msgMap) {
        this.msgMap = msgMap;
    }

    public BaseEvent(Object source, Integer userId, Integer eventType, Date catchTime, String facePicUrl, String backgroundPicUrl, String videoUrl, String openType, Integer openFlg, String deviceSn, String faceMatchDegree, String cardNo, String analysisType, Map<String, Object> msgMap,BaseDevice baseDevice) {
        super(source);
        this.userId = userId;
        this.eventType = eventType;
        this.catchTime = catchTime;
        this.facePicUrl = facePicUrl;
        this.backgroundPicUrl = backgroundPicUrl;
        this.videoUrl = videoUrl;
        this.openType = openType;
        this.openFlg = openFlg;
        this.deviceSn = deviceSn;
        this.faceMatchDegree = faceMatchDegree;
        this.cardNo = cardNo;
        this.analysisType = analysisType;
        this.msgMap = msgMap;
        this.baseDevice = baseDevice;
    }

    public BaseEvent(Object source, Integer userId, Integer eventType, Date catchTime, String facePicUrl, String backgroundPicUrl, String openType, Integer openFlg, String deviceSn, String faceMatchDegree, String cardNo, String analysisType, String videoUrl) {
       this(source,userId,eventType,catchTime, facePicUrl, backgroundPicUrl, videoUrl, openType, openFlg, deviceSn, faceMatchDegree, cardNo, analysisType, null,null);
    }
    public BaseEvent(Object source, Integer userId, Integer eventType, Date catchTime, String facePicUrl, String backgroundPicUrl, String openType, Integer openFlg,String deviceSn, String faceMatchDegree, String cardNo, String analysisType) {
        this(source, userId, eventType, catchTime, facePicUrl,  backgroundPicUrl,  openType,  openFlg, deviceSn,  faceMatchDegree,  cardNo,  analysisType,null);
    }
    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAnalysisType() {
        return analysisType;
    }

    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }


    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public Date getCatchTime() {
        return catchTime;
    }

    public void setCatchTime(Date catchTime) {
        this.catchTime = catchTime;
    }

    public String getFacePicUrl() {
        return facePicUrl;
    }

    public void setFacePicUrl(String facePicUrl) {
        this.facePicUrl = facePicUrl;
    }

    public String getBackgroundPicUrl() {
        return backgroundPicUrl;
    }

    public void setBackgroundPicUrl(String backgroundPicUrl) {
        this.backgroundPicUrl = backgroundPicUrl;
    }

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }

    public Integer getOpenFlg() {
        return openFlg;
    }

    public void setOpenFlg(Integer openFlg) {
        this.openFlg = openFlg;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getFaceMatchDegree() {
        return faceMatchDegree;
    }

    public void setFaceMatchDegree(String faceMatchDegree) {
        this.faceMatchDegree = faceMatchDegree;
    }

    public BaseDevice getBaseDevice() {
        return baseDevice;
    }

    public void setBaseDevice(BaseDevice baseDevice) {
        this.baseDevice = baseDevice;
    }
}
