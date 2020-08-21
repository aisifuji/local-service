package com.herocheer.zhsq.localservice.core.device.HaCamera;

import com.ha.facecamera.configserver.pojo.Face;
import com.ha.facecamera.configserver.pojo.Gateopening;
import com.herocheer.zhsq.localservice.core.device.AbstractDevice;
import com.herocheer.zhsq.localservice.core.device.entity.*;
import com.herocheer.zhsq.localservice.core.exception.CheckedException;
import com.herocheer.zhsq.localservice.core.util.FileUtil;
import com.herocheer.zhsq.localservice.core.util.ImageUtil;
import com.herocheer.zhsq.localservice.core.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Component
public class HaGuardDevice extends AbstractDevice {

    @Autowired
    private HaSdkHandler haSdkHandler;

    //设备类型
    private static final Integer deviceType = 10;
    //设备厂商标志
    private static final Integer brand = 40;
    //设备支持功能
    private static final Integer deviceSupFun1 = 00; //二维码
    private static final Integer deviceSupFun2 = 10; //人脸
    private static final Integer deviceSupFun3 = 20; //ID卡
    private static final Integer deviceSupFun4 = 40; //测温
    private static final Integer deviceSupFun5 = 50; //口罩
    private static final Integer deviceSupFun6 = 60; //远程开门

    private static final Logger logger = LoggerFactory.getLogger(HaGuardDevice.class);

    private volatile Boolean isOpenCapture = false;

    protected HaGuardDevice() {
        super(deviceType, brand, Arrays.asList(deviceSupFun1,deviceSupFun2,deviceSupFun3,deviceSupFun4,deviceSupFun5,deviceSupFun6));
    }


//    @Override
//    public DeviceResponse registerDevice(BaseDevice baseDevice) {
//        DeviceResponse online = this.isOnline(baseDevice);
//        if(online.getIsSuccess()){
//            //注册成功启动抓拍
//            captureEvent(baseDevice);
//        }
//        return online;
//    }

    @Override
    public DeviceResponse doRegisterDevice(BaseDevice baseDevice) {
        return this.isOnline(baseDevice);
    }

    @Override
    public DeviceResponse setFace(BaseDevice baseDevice, BaseFace baseFace) {
        return setOrUpdateOrDelFace(baseDevice,baseFace,"set");
    }

    @Override
    public DeviceResponse updateFace(BaseDevice baseDevice, BaseFace baseFace) {
        return setOrUpdateOrDelFace(baseDevice,baseFace,"update");
    }

    @Override
    public DeviceResponse delFace(BaseDevice baseDevice, BaseFace baseFace) {
        return setOrUpdateOrDelFace(baseDevice,baseFace,"del");
    }

    @Override
    public DeviceResponse setCard(BaseDevice baseDevice, BaseCard baseCard) {
        baseCard.setUserId(baseCard.getUserId()+"_"+baseCard.getCardNo());
        return setOrUpdateOrDelCard(baseDevice,baseCard,"set");
    }

    @Override
    public DeviceResponse updateCard(BaseDevice baseDevice, BaseCard baseCard) {
        baseCard.setUserId(baseCard.getUserId()+"_"+baseCard.getCardNo());
        return setOrUpdateOrDelCard(baseDevice,baseCard,"update");
    }

    @Override
    public DeviceResponse delCard(BaseDevice baseDevice, BaseCard baseCard) {
        baseCard.setUserId(baseCard.getUserId()+"_"+baseCard.getCardNo());
        return setOrUpdateOrDelCard(baseDevice,baseCard,"del");
    }

//    @Override
//    public Integer getDeviceType() {
//        return this.deviceType;
//
//    }
//
//    @Override
//    public List<Integer> getDeviceSupFun() {
//        List<Integer> typeList = new ArrayList<>();
//        typeList.add(deviceSupFun1);
//        typeList.add(deviceSupFun2);
//        typeList.add(deviceSupFun3);
//        return typeList;
//    }

    @Override
    public Boolean isSupportSetFace() {
        return true;
    }

    @Override
    public Boolean isSupportSetCard() {
        return true;
    }

    @Override
    public Boolean isSupportCapture() {
        return true;
    }

    @Override
    public void captureEvent(BaseDevice baseDevice) {
        isSupportCapture();
//        if(isOpenCapture){
//            return;
//        }
        logger.info("======================启动抓拍================");
        haSdkHandler.getDataServer().onCaptureCompareDataReceived((data)->{
            logger.info("======================抓拍成功，用户编号data.getPersonID()：" + data.getPersonID());
            logger.info("======================抓拍成功，用户卡号data.getWiegandNo1()：" + data.getWiegandNo1());
            logger.info("======================抓拍成功，人像匹配度data.getMatchScore()：" + data.getMatchScore());
//            isOpenCapture = true;
            //二维码
            try {
            if(null != data.getCodetype() && null != data.getCodetext()){
                String codeText = data.getCodetext();
                BaseEvent baseEvent =  new BaseEvent(this,null,3,data.getCaptureTime(),
                        null,null,null,null,null,null,null,codeText,null,null,baseDevice);
                SpringUtil.getApplicationContext().publishEvent(baseEvent);
                return;
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String openDoorType,filename,facePath,backgroundPath;
            Date capTime = data.getCaptureTime();
            String date = df.format(capTime);
            String datetime = sdf.format(capTime);
            String preFacePath = haSdkHandler.getCatchFacePicPath() + date +"/";
            String preBackground = haSdkHandler.getCatchBackgroundPicPath() + date +"/";

            if((data.getMatchScore()>0&&data.getMatchScore()<100&&!StringUtils.isEmpty(data.getPersonID()))||(data.getTemperature()>0.0&&!StringUtils.isEmpty(data.getPersonID()))){
                openDoorType = "10";//刷脸
                filename = "ID_" + data.getPersonID() + "_" + datetime + ".jpg";
            }else if(data.getMatchScore()==100&&data.getTemperature()==0.0&&!StringUtils.isEmpty(data.getPersonID())){
                //刷卡模式
                openDoorType = "20";//刷卡
                filename = "CD_" + data.getPersonID() + "_" + datetime + ".jpg";
            }else {
                //陌生人
                openDoorType = "10";//刷脸
                filename = "MSR_" + datetime + ".jpg";
            }
            Boolean isPutFace = false;
            Boolean isPutBackFace = false;
            facePath = preFacePath + filename;
            backgroundPath = preBackground + filename;
            File faceDir = new File(preFacePath);
            File bkDir = new File(preBackground);
            FileUtil.judeDirExists(faceDir);
            FileUtil.judeDirExists(bkDir);
            //保存图片
            byte[] featureImageByte = data.getFeatureImageData();
            byte[] environmentImageData = data.getEnvironmentImageData();
            if (null != featureImageByte) {
                isPutFace = true;
                ImageUtil.byteToImage(featureImageByte, facePath);
            }
            if (null != environmentImageData) {
                isPutBackFace = true;
                ImageUtil.byteToImage(environmentImageData, backgroundPath);
            }
            String userId = data.getPersonID();
            if(null!=userId&&userId.indexOf("_")>-1){
                userId = userId.substring(0, userId.indexOf("_"));
            }
            BaseEvent baseEvent =  new BaseEvent(this,userId==null?null:Integer.parseInt(userId),1,data.getCaptureTime(),
                    isPutFace?facePath:null,isPutBackFace?backgroundPath:null,openDoorType,StringUtils.isEmpty(data.getPersonID())?0:1,data.getSn(),String.valueOf(data.getMatchScore()),String.valueOf(data.getWiegandNo1()),null);
            SpringUtil.getApplicationContext().publishEvent(baseEvent);
            } catch (Exception e){
//                isOpenCapture = false;
                logger.error(e.getMessage());
            }
        });
    }

    @Override
    public DeviceResponse isOnline(BaseDevice baseDevice) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean cameraOnlineState = haSdkHandler.getConfigServer().getCameraOnlineState(baseDevice.getDeviceSn());
        logger.info("设备序列号：{}，在线情况：{}",baseDevice.getDeviceSn(), cameraOnlineState?"在线":"离线");

        return new DeviceResponse(cameraOnlineState,"返回码："+ haSdkHandler.getConfigServer().getLastErrorCode()+"返回信息:"+ haSdkHandler.getConfigServer().getLastErrorMsg());
    }

    public void setVoice(String deviceSn,String successVoice){
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        haSdkHandler.getConfigServer().setVoiceplayback(deviceSn, successVoice, haSdkHandler.getTimeoutInMs());
    }

    @Override
    public DeviceResponse openGuardOrder(BaseDevice baseDevice) {
        if(10!=this.getDeviceType()){
            throw new CheckedException("device-0007");
        }
        Gateopening gateopening = new Gateopening();
        gateopening.setPort((byte)1);
        gateopening.setDirection((byte)2);
        gateopening.setControltype((byte)1);
        gateopening.setControlpattern((byte)1);
        gateopening.setControlid((byte)11);
        gateopening.setEmpid("userSelf");
        boolean isSuccess = haSdkHandler.getConfigServer().setGateopen(baseDevice.getDeviceSn(), gateopening, haSdkHandler.getTimeoutInMs());
        if(isSuccess){
            setVoice(baseDevice.getDeviceSn(), haSdkHandler.getSuccessVoice());
        }
        return new DeviceResponse(isSuccess,"返回码："+ haSdkHandler.getConfigServer().getLastErrorCode()+"返回信息:"+ haSdkHandler.getConfigServer().getLastErrorMsg());
    }

    public DeviceResponse setOrUpdateOrDelFace(BaseDevice baseDevice, BaseFace baseFace,String op){
        Face face = new Face();
        face.setId(baseFace.getUserId());
        face.setName(baseFace.getName());
        face.setRole(baseFace.getRole()==10?1:2);
        face.setJpgFilePath(new String[]{baseFace.getJpgFilePath()});
        face.setStartDate(baseFace.getEffStartTime());
        face.setExpireDate(baseFace.getEffEndTime());
        return doSetOrUpdateOrDelFace(baseDevice.getDeviceSn(),face,op);
    }

    public DeviceResponse setOrUpdateOrDelCard(BaseDevice baseDevice, BaseCard baseCard,String op){
        Face face = new Face();
        face.setId(baseCard.getUserId());
        face.setName(baseCard.getName());
        //下卡默认是白名单
        face.setRole(1);
        face.setWiegandNo(Long.valueOf(baseCard.getCardNo()));
        face.setJpgFilePath(new String[]{haSdkHandler.getDefaultFacePicPath()});
        face.setStartDate(baseCard.getEffStartTime());
        face.setExpireDate(baseCard.getEffEndTime());
        return doSetOrUpdateOrDelFace(baseDevice.getDeviceSn(),face,op);
    }

    public DeviceResponse doSetOrUpdateOrDelFace(String deviceSn,Face face,String op){
        boolean isSuccess = false;
        if("set".equals(op)){
            isSuccess = haSdkHandler.getConfigServer().addFace(deviceSn, face, haSdkHandler.getTimeoutInMs());
        }else if("update".equals(op)){
            isSuccess = haSdkHandler.getConfigServer().modifyFace(deviceSn, face, haSdkHandler.getTimeoutInMs());
        }else if("del".equals(op)){
            isSuccess = haSdkHandler.getConfigServer().deleteFace(deviceSn, face.getId(), haSdkHandler.getTimeoutInMs());
        }else {
            throw new CheckedException("device-0008");
        }
        return new DeviceResponse(isSuccess,"返回码："+ haSdkHandler.getConfigServer().getLastErrorCode()+"返回信息:"+ haSdkHandler.getConfigServer().getLastErrorMsg());
    }
}
