package com.herocheer.zhsq.localservice.impl.service.impl;

import com.herocheer.zhsq.localservice.core.device.HaCamera.HaGuardDevice;
import com.herocheer.zhsq.localservice.core.device.box.YTBox.YTBoxDevice;
import com.herocheer.zhsq.localservice.core.device.entity.BaseDevice;
import com.herocheer.zhsq.localservice.core.device.entity.BaseEvent;
import com.herocheer.zhsq.localservice.core.device.entity.BaseFace;
import com.herocheer.zhsq.localservice.core.device.entity.DeviceResponse;
import com.herocheer.zhsq.localservice.core.exception.CheckedException;
import com.herocheer.zhsq.localservice.core.util.ImageUtil;
import com.herocheer.zhsq.localservice.core.util.SpringUtil;
import com.herocheer.zhsq.localservice.impl.config.CommonProperties;
import com.herocheer.zhsq.localservice.impl.domain.constant.CommunityInfo;
import com.herocheer.zhsq.localservice.impl.domain.entity.*;
import com.herocheer.zhsq.localservice.impl.mapper.BehaviorAnalysisAlarmMapper;
import com.herocheer.zhsq.localservice.impl.service.*;
import com.herocheer.zhsq.localservice.impl.support.CustomRestTemplate;
import com.herocheer.zhsq.localservice.impl.support.keygenerate.SequenceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service("deviceEventListenerService")
public class DeviceEventListenerServiceImpl implements DeviceEventListenerService {

    @Autowired
    private GuardOpenRecordMsgService guardOpenRecordMsgService;
    @Autowired
    private PersonPhotoFaceRecordService personPhotoFaceRecordService;
    @Autowired
    private CommonProperties commonProperties;
    @Autowired
    private FaceMsgService faceMsgService;
    @Autowired
    private BehaviorAnalysisAlarmMapper behaviorAnalysisAlarmMapper;
    @Autowired
    private CustomRestTemplate customRestTemplate;
    @Autowired
    private SequenceFactory sequenceFactory;
    @Autowired
    private GuardCardMsgService guardCardMsgService;

    private static final Logger logger = LoggerFactory.getLogger(DeviceEventListenerServiceImpl.class);


    @Override
    public void onApplicationEvent(BaseEvent baseEvent) {
        switch (baseEvent.getEventType()) {
            //抓拍事件
            case 0:
                PersonPhotoFaceRecord personPhoto = new PersonPhotoFaceRecord();
                personPhoto.setFacePicUrl(baseEvent.getFacePicUrl());
//                personPhoto.setCommunityId(CommunityInfo.gatherSystemCode);
                personPhoto.setAccessFaceUrl(baseEvent.getFacePicUrl());
                personPhoto.setBackgroundPicUrl(baseEvent.getBackgroundPicUrl());
                personPhoto.setCatchTime(baseEvent.getCatchTime());
                if(baseEvent.getMsgMap().containsKey("ageRange")){
                    personPhoto.setAgeRange(String.valueOf(baseEvent.getMsgMap().get("ageRange")));
                }
                if(baseEvent.getMsgMap().containsKey("sex")){
                    personPhoto.setSex(Integer.parseInt(String.valueOf(baseEvent.getMsgMap().get("sex"))));
                }
                personPhoto.setFaceMatchDegree(baseEvent.getFaceMatchDegree());
                personPhoto.setUserId(baseEvent.getUserId());
                personPhoto.setDeviceSn(baseEvent.getDeviceSn());
                FaceMsg faceInfo = null;
                if(null != personPhoto.getUserId()){
                    faceInfo = faceMsgService.queryOneByUserId(baseEvent.getUserId());
                }
                //判断是否陌生人
                if (null != personPhoto.getUserId()&&null!=faceInfo) {
//                    checkPersonDatasource(personPhoto,faceMsg);
                    personPhoto.setUserName(faceInfo.getUserName());
                    personPhoto.setUserIdentity(faceInfo.getLiveIdentity());
                    personPhoto.setDataSource(faceInfo.getDataSource());
                    personPhoto.setPersonTagGroup(faceInfo.getPersonTagGroup());
                    personPhoto.setPersonType(faceInfo.getPersonType());
                }else {
                    //设备类型是智脑盒子
                    personPhoto.setUserIdentity("50");
                    personPhoto.setDataSource("30");
                    personPhoto.setPersonType("30");
                    if(null!=baseEvent.getMsgMap()&&30==Integer.parseInt(String.valueOf(baseEvent.getMsgMap().get("deviceType")))){
                        //智脑盒子设置陌生人id
                        personPhoto.setCommunityCode(CommunityInfo.gatherSystemCode);
                        Integer strangerUserId = getStrangerUserId(personPhoto);
                        personPhoto.setCommunityCode(null);
                        personPhoto.setUserId(strangerUserId);
                        personPhoto.setUserName("陌生人_"+strangerUserId);
                        //获取智脑设备api
                        YTBoxDevice bean = SpringUtil.getBean(YTBoxDevice.class);
                        //下发人脸
                        BaseFace baseFace = new BaseFace(String.valueOf(personPhoto.getUserId()),personPhoto.getUserName(),1,personPhoto.getFacePicUrl(),null,null);
                        BaseDevice baseDevice = new BaseDevice(null,
                                String.valueOf(baseEvent.getMsgMap().get("deviceSn")),
                                String.valueOf(baseEvent.getMsgMap().get("deviceIp")),String.valueOf(baseEvent.getMsgMap().get("deviceAccount")),String.valueOf(baseEvent.getMsgMap().get("devicePassword")),Integer.parseInt(String.valueOf(baseEvent.getMsgMap().get("devicePort"))));
                        DeviceResponse deviceResponse = bean.setFace(baseDevice, baseFace);
                        //成功存DB
                        if(deviceResponse.getIsSuccess()){
                            FaceMsg faceMsg = new FaceMsg();
                            faceMsg.setUserId(personPhoto.getUserId());
                            faceMsg.setDeviceSn(baseDevice.getDeviceSn());
                            faceMsg.setDataSource(personPhoto.getDataSource());
                            faceMsg.setOpType("40");
                            faceMsg.setFaceImgUrl(personPhoto.getFacePicUrl());
                            faceMsg.setUserName(personPhoto.getUserName());
                            faceMsg.setLiveIdentity("50");
                            faceMsg.setPersonType("30");
                            faceMsgService.saveOrUpdateFaceMsg(faceMsg);
                        }
                    }
                }
                personPhotoFaceRecordService.saveOrUpdatePersonPhotoFaceRecord(personPhoto);
                //处理图片
                String personCatchFace = ImageUtil.getImgStr(personPhoto.getFacePicUrl());
                String personBackgroundCatchFace = ImageUtil.getImgStr(personPhoto.getBackgroundPicUrl());
                personPhoto.setBackgroundPic(personBackgroundCatchFace);
                personPhoto.setFacePic(personCatchFace);
                customRestTemplate.execute(commonProperties.getUploadCaptureEvent(), HttpMethod.POST, personPhoto, String.class);
                break;
            //门禁开门事件
            case 1:
                GuardOpenRecordMsg guardOpenRecordMsg = new GuardOpenRecordMsg();
                PersonPhotoFaceRecord personPhotoFaceRecord = new PersonPhotoFaceRecord();
//                personPhotoFaceRecord.setCommunityId(CommunityInfo.gatherSystemCode);
                personPhotoFaceRecord.setAccessFaceUrl(baseEvent.getFacePicUrl());
                BeanUtils.copyProperties(baseEvent, guardOpenRecordMsg);
                FaceMsg guardFace = null;
                GuardCardMsg guardCardOpenMsg = null;
                if(null != baseEvent.getUserId()){
                    guardFace = faceMsgService.queryOneByUserId(baseEvent.getUserId());
                    if(null==guardFace&& StringUtils.isEmpty(baseEvent.getCardNo())){
                         guardCardOpenMsg = guardCardMsgService.queryOneByUserIdAndCardNo(String.valueOf(baseEvent.getUserId()), baseEvent.getCardNo());
                    }
                }
                //非陌生人
                if (null != baseEvent.getUserId()&&null!=guardFace) {
//                    FaceMsg face = faceMsgService.queryOneByUserId(guardOpenRecordMsg.getUserId());
                    guardOpenRecordMsg.setOnlineFlg(guardFace.getOnlineFlg());
                    guardOpenRecordMsg.setUserName(guardFace.getUserName());
                    guardOpenRecordMsg.setAddress(guardFace.getAddress());
                    guardOpenRecordMsg.setMobile(guardFace.getMobile());
                    guardOpenRecordMsg.setUserIdentity(guardFace.getLiveIdentity());
                    personPhotoFaceRecord.setDataSource(guardFace.getDataSource());
                    personPhotoFaceRecord.setPersonTagGroup(guardFace.getPersonTagGroup());
                    personPhotoFaceRecord.setPersonType(guardFace.getPersonType());
//                    checkPersonDatasource(personPhotoFaceRecord,face);
                }else if(null != baseEvent.getUserId()&&null!=guardCardOpenMsg){
                    guardOpenRecordMsg.setOnlineFlg(guardCardOpenMsg.getOnlineFlg());
                    guardOpenRecordMsg.setUserName(guardCardOpenMsg.getUserName());
                    guardOpenRecordMsg.setAddress(guardCardOpenMsg.getAddress());
                    guardOpenRecordMsg.setMobile(guardCardOpenMsg.getMobile());
                    guardOpenRecordMsg.setUserIdentity(guardCardOpenMsg.getLiveIdentity());
                } else  {
                    personPhotoFaceRecord.setCommunityCode(CommunityInfo.gatherSystemCode);
                    Integer strangerUserId = getStrangerUserId(personPhotoFaceRecord);
                    personPhotoFaceRecord.setCommunityCode(null);
                    guardOpenRecordMsg.setUserIdentity("50");
                    guardOpenRecordMsg.setUserId(strangerUserId);
                    guardOpenRecordMsg.setUserName("陌生人_"+strangerUserId);
                    personPhotoFaceRecord.setDataSource("30");
                    personPhotoFaceRecord.setPersonType("30");
                }
                BeanUtils.copyProperties(guardOpenRecordMsg, personPhotoFaceRecord);
                //图片处理
                if(null!=guardOpenRecordMsg.getFacePicUrl()){
                    String guardFacePicBase64 = ImageUtil.getImgStr(guardOpenRecordMsg.getFacePicUrl());
                    guardOpenRecordMsg.setFacePic(guardFacePicBase64);
                }
                if(null!=guardOpenRecordMsg.getBackgroundPicUrl()){
                    String guardBackgroundPicBase64 = ImageUtil.getImgStr(guardOpenRecordMsg.getBackgroundPicUrl());
                    guardOpenRecordMsg.setBackgroundPic(guardBackgroundPicBase64);
                }
                if(null!=personPhotoFaceRecord.getFacePicUrl()){
                    String personCatchFaceBase64 = ImageUtil.getImgStr(personPhotoFaceRecord.getFacePicUrl());
                    personPhotoFaceRecord.setFacePic(personCatchFaceBase64);
                }
                if(null!=personPhotoFaceRecord.getBackgroundPicUrl()){
                    String personBackgroundCatchFaceBase64 = ImageUtil.getImgStr(personPhotoFaceRecord.getBackgroundPicUrl());
                    personPhotoFaceRecord.setBackgroundPic(personBackgroundCatchFaceBase64);
                }
//                String guardFacePicBase64 = ImageUtil.getImgStr(guardOpenRecordMsg.getFacePicUrl());
//                String guardBackgroundPicBase64 = ImageUtil.getImgStr(guardOpenRecordMsg.getBackgroundPicUrl());
//                String personCatchFaceBase64 = ImageUtil.getImgStr(personPhotoFaceRecord.getFacePicUrl());
//                String personBackgroundCatchFaceBase64 = ImageUtil.getImgStr(personPhotoFaceRecord.getBackgroundPicUrl());
//                guardOpenRecordMsg.setFacePic(guardFacePicBase64);
//                guardOpenRecordMsg.setBackgroundPic(guardBackgroundPicBase64);
//                personPhotoFaceRecord.setBackgroundPic(personCatchFaceBase64);
//                personPhotoFaceRecord.setFacePic(personBackgroundCatchFaceBase64);
                personPhotoFaceRecordService.saveOrUpdatePersonPhotoFaceRecord(personPhotoFaceRecord);
                customRestTemplate.execute(commonProperties.getUploadCaptureEvent(), HttpMethod.POST, personPhotoFaceRecord, String.class);
                logger.info("开始上传。。。");
                if(!"50".equals(guardOpenRecordMsg.getUserIdentity())){
                    guardOpenRecordMsgService.saveOrUpdateGuardOpenRecordMsg(guardOpenRecordMsg);
                    customRestTemplate.execute(commonProperties.getUploadGuardDoorEvent(), HttpMethod.POST, guardOpenRecordMsg, String.class);
                }
                break;
            //行为分析仪
            case 2:
                BehaviorAnalysisAlarm behaviorAnalysisAlarm=new BehaviorAnalysisAlarm();
                behaviorAnalysisAlarm.setEventName(baseEvent.getAnalysisType());
                behaviorAnalysisAlarm.setAlarmTime(baseEvent.getCatchTime());
                behaviorAnalysisAlarm.setDeviceSn(baseEvent.getDeviceSn());
                behaviorAnalysisAlarm.setPicUrl(baseEvent.getFacePicUrl());
                behaviorAnalysisAlarm.setVideoUrl(baseEvent.getVideoUrl());
                behaviorAnalysisAlarmMapper.insert(behaviorAnalysisAlarm);
                AnalysisAlarmVo vo=new AnalysisAlarmVo();
                BeanUtils.copyProperties(behaviorAnalysisAlarm,vo);
                if(baseEvent.getFacePicUrl()!=null){
                    vo.setPic(ImageUtil.getImgStr(baseEvent.getFacePicUrl()));
                }
                List<String> imgs=new ArrayList<String>();
                if(baseEvent.getVideoUrl()!=null){
                    imgs.add(ImageUtil.getVideoStr(baseEvent.getVideoUrl()));
                }
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(vo.getAlarmTime());
                String content="["+dateString+"]发生["+vo.getEventName()+"]";
                vo.setEventContent(content);
                logger.info("上传行为分析仪事件。。。");
                customRestTemplate.execute(commonProperties.getUploadAnalysisEvent(), HttpMethod.POST, vo, String.class,imgs);
                logger.info("上传行为分析仪事件完成");
                break;
             //DNKE门禁二维码事件
            case 3:
                GuardCardMsg guardCardMsg = guardCardMsgService.queryOneByDeviceSnAndCardNo(baseEvent.getBaseDevice().getDeviceSn(), baseEvent.getCardNo());

                HaGuardDevice bean = SpringUtil.getBean(HaGuardDevice.class);
               //没有卡
                if(guardCardMsg==null){
                    bean.setVoice(baseEvent.getBaseDevice().getDeviceSn(),"未授权");
                    break;
                }
                //卡过期
                if(guardCardMsg!=null&&guardCardMsg.getEffEndTime().before(new Date())){
                    bean.setVoice(baseEvent.getBaseDevice().getDeviceSn(),"卡过期");
                    break;
                }
                //开门
                if(guardCardMsg!=null&&guardCardMsg.getEffEndTime().after(new Date())){
                    bean.openGuardOrder(baseEvent.getBaseDevice());
                    GuardOpenRecordMsg guardOpenRecord = new GuardOpenRecordMsg();
                    guardOpenRecord.setOnlineFlg(guardCardMsg.getOnlineFlg());
                    guardOpenRecord.setUserName(guardCardMsg.getUserName());
                    guardOpenRecord.setAddress(guardCardMsg.getAddress());
                    guardOpenRecord.setMobile(guardCardMsg.getMobile());
                    guardOpenRecord.setUserIdentity(guardCardMsg.getLiveIdentity());
                    Assert.notNull(guardCardMsg.getUserId(),"userId is null");
                    guardOpenRecord.setUserId(guardCardMsg.getUserId());
                    //40访客
                    if("40".equals(guardCardMsg.getLiveIdentity())){
                        String userId = String.valueOf(guardCardMsg.getUserId());
                        if(userId.startsWith("111")){
                            guardOpenRecord.setUserId(Integer.parseInt(userId.substring(3)));
                        }else {
                            logger.error("访问id异常：访客id开头必须为111");
                            throw new CheckedException("sys-0007");
                        }
                    }
                    customRestTemplate.execute(commonProperties.getUploadGuardDoorEvent(), HttpMethod.POST, guardOpenRecord, String.class);
                    break;
                }
                break;

        }


    }

    public Integer getStrangerUserId(PersonPhotoFaceRecord personPhoto){
        PersonPhotoFaceRecord responseMap = customRestTemplate.execute(commonProperties.getStrangerUploadApi(), HttpMethod.POST, personPhoto, PersonPhotoFaceRecord.class).getBody();
        Integer userId = null;
        if(responseMap!=null&&responseMap.getId()!=null){
            userId = responseMap.getId();
        }else {
            logger.error("上传陌生人接口异常，陌生人id为空");
            throw new CheckedException("sys-0006");
        }
        return userId;
    }

    private void checkPersonDatasource(PersonPhotoFaceRecord personPhotoFaceRecord,FaceMsg faceMsg){
        if(null!=faceMsg.getOnlineFlg()){
            switch (faceMsg.getOnlineFlg()){
                case 0: personPhotoFaceRecord.setDataSource("20");break;
                case 1: personPhotoFaceRecord.setDataSource("10");break;
                default: personPhotoFaceRecord.setDataSource("40");break;
            }
        }else {
            personPhotoFaceRecord.setDataSource("40");
        }
    }
}
