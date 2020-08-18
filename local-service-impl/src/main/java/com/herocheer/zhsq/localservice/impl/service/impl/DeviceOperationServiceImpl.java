package com.herocheer.zhsq.localservice.impl.service.impl;

import com.herocheer.zhsq.localservice.core.device.DefaultDeviceFactory;
import com.herocheer.zhsq.localservice.core.device.Device;
import com.herocheer.zhsq.localservice.core.device.entity.BaseCard;
import com.herocheer.zhsq.localservice.core.device.entity.BaseDevice;
import com.herocheer.zhsq.localservice.core.device.entity.BaseFace;
import com.herocheer.zhsq.localservice.core.device.entity.DeviceResponse;
import com.herocheer.zhsq.localservice.core.exception.CheckedException;
import com.herocheer.zhsq.localservice.core.util.BASE64DecodedMultipartFile;
import com.herocheer.zhsq.localservice.core.util.FileUtil;
import com.herocheer.zhsq.localservice.core.util.SerialNoUtil;
import com.herocheer.zhsq.localservice.impl.config.CommonProperties;
import com.herocheer.zhsq.localservice.impl.domain.entity.*;
import com.herocheer.zhsq.localservice.impl.service.*;
import com.herocheer.zhsq.localservice.impl.support.keygenerate.SequenceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("deviceOperationService")
public class DeviceOperationServiceImpl implements DeviceOperationService {


    @Autowired
    private DeviceStatusMsgService deviceStatusMsgService;
    @Autowired
    private CommonProperties commonProperties;
    @Autowired
    private DeviceRegisterService registerService;
    @Autowired
    private FaceMsgService faceMsgService;
    @Autowired
    private DeviceOrderResService deviceOrderResService;
    @Autowired
    private GuardCardMsgService guardCardMsgService;
    @Autowired
    private SequenceFactory sequenceFactory;

    private static final Logger logger = LoggerFactory.getLogger(DeviceOperationServiceImpl.class);

    /**
     * 注册业务
     * @param deviceRegister
     * @return
     */
    @Override
    public DeviceResponse registerDevice(DeviceRegister deviceRegister) {
        Assert.hasText(deviceRegister.getDeviceSn(),"deviceSn is empty");
        Assert.hasText(deviceRegister.getLoginIp(),"deviceIp is empty");
        Assert.hasText(deviceRegister.getAccount(),"device account is empty");
        Assert.hasText(deviceRegister.getPassword(),"device password is empty");
        //获取设备实例
        Device device = getDeviceInstance(deviceRegister);
        BaseDevice baseDevice = new BaseDevice(deviceRegister.getDeviceSn(),deviceRegister.getLoginIp(),deviceRegister.getAccount(),deviceRegister.getPassword(),deviceRegister.getPort());
        //设备注册
        DeviceResponse deviceResponse = device.registerDevice(baseDevice);
        logger.info("设备序列号注册：{},{}",deviceRegister.getDeviceSn(), deviceResponse.getMessage());
        //保存数据库
        if(deviceResponse.getIsSuccess()){
            deviceRegister.setNewKeyid(deviceRegister.getId());
            deviceRegister.setId(null);
            deviceRegister.setOnline(1);
            registerService.saveOrUpdateDeviceRegister(deviceRegister);
            //启动抓拍事件交个各个设备实例来处理
            //   device.captureEvent(baseDevice);
        }
        return deviceResponse;

    }

    /**
     * 获取设备心跳
     * @param deviceRegister
     */
    @Override
    public void getDeviceStatus(DeviceRegister deviceRegister) {
        Assert.hasText(deviceRegister.getDeviceSn(),"deviceSn is empty");
        Assert.hasText(deviceRegister.getLoginIp(),"deviceIp is empty");
        Assert.hasText(deviceRegister.getAccount(),"device account is empty");
        Assert.hasText(deviceRegister.getPassword(),"device password is empty");
        Device device = this.getDeviceInstance(deviceRegister);
        BaseDevice baseDevice = new BaseDevice(deviceRegister.getDeviceSn(),deviceRegister.getLoginIp(),deviceRegister.getAccount(),deviceRegister.getPassword(),deviceRegister.getPort());
        Integer isOnline = device.isOnline(baseDevice).getIsSuccess()?1:0;
        DeviceStatusMsg deviceStatusMsg = new DeviceStatusMsg();
        deviceStatusMsg.setDeviceSn(deviceRegister.getDeviceSn());
        deviceStatusMsg.setStatus(isOnline);
        deviceStatusMsgService.saveOrUpdateDeviceStatusMsg(deviceStatusMsg);
        deviceRegister.setOnline(isOnline);
        deviceRegister.setId(deviceRegister.getNewKeyid());
}

    /**
     * 下人脸业务
     * @param deviceRegister
     * @param faceMsg
     * @return
     */
    @Override
    public DeviceResponse setFace(DeviceRegister deviceRegister, FaceMsg faceMsg) throws ParseException {
        Assert.notNull(deviceRegister,"deviceRegister is null");
        Assert.notNull(faceMsg,"faceMsg is null");
        Assert.hasText(deviceRegister.getDeviceSn(),"deviceSn is empty");
        Assert.hasText(faceMsg.getFaceImg(),"faceImg is empty");
        Assert.notNull(faceMsg.getUserId(),"faceUserId is empty");
        Assert.notNull(faceMsg.getId(),"faceId is empty");
        //获取设备详情
        DeviceRegister deviceInfo = queryDevice(deviceRegister.getDeviceSn());
        //获取设备实例
        Device device = getDeviceInstance(deviceInfo);
        BaseDevice baseDevice = new BaseDevice(deviceInfo.getDeviceSn(),deviceInfo.getLoginIp(),deviceInfo.getAccount(),deviceInfo.getPassword(),deviceInfo.getPort());
        String picPath = FileUtil.makeDynmicsDateDir(commonProperties.getPicWhiteDownloadLocalPath()+SerialNoUtil.getSerialNo())+".jpg";
        //黑名单
        if("20".equals(faceMsg.getPersonType())){
            picPath = FileUtil.makeDynmicsDateDir(commonProperties.getPicBlackDownloadLocalPath()+SerialNoUtil.getSerialNo()+".jpg");
        }
        MultipartFile multipartFile = BASE64DecodedMultipartFile.base64ToMultipart(faceMsg.getFaceImg());
        try {
            multipartFile.transferTo(new File(picPath));
        } catch (IOException e) {
            throw new CheckedException("sys-0002",e.getMessage());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(StringUtils.isEmpty(faceMsg.getPersonType())){
            faceMsg.setPersonType("10");
        }
        if(null==faceMsg.getEffStartTime()){
            faceMsg.setEffStartTime(new Date());
        }
        if(null==faceMsg.getEffEndTime()){
            faceMsg.setEffEndTime(sdf.parse("2029-12-31 23:59:59"));
        }
        BaseFace baseFace = new BaseFace(String.valueOf(faceMsg.getUserId()),faceMsg.getUserName(), Integer.valueOf(faceMsg.getPersonType())
                ,picPath,faceMsg.getEffStartTime(),faceMsg.getEffEndTime());
        DeviceResponse deviceResponse = null;
        //根据操作类型下发图片
        if("10".equals(faceMsg.getOpType())){
            logger.info("开始下发人脸。。。");
            deviceResponse = device.setFace(baseDevice, baseFace);
        }else if("20".equals(faceMsg.getOpType())){
            logger.info("开始修改人脸。。。");
            deviceResponse = device.updateFace(baseDevice, baseFace);
        }else if("30".equals(faceMsg.getOpType())){
            logger.info("开始删除人脸。。。");
            deviceResponse = device.delFace(baseDevice, baseFace);
        }else {
            throw new CheckedException("device-0008");
        }
        //成功下发才保存  更新下发ID
        faceMsg.setNewKeyid(faceMsg.getId());
        faceMsg.setFaceImgUrl(picPath);
        if(deviceResponse.getIsSuccess()){
            if("10".equals(faceMsg.getOpType())){
                faceMsg.setId(null);
                faceMsgService.saveOrUpdateFaceMsg(faceMsg);
            }else if("20".equals(faceMsg.getOpType())){
                faceMsgService.saveOrUpdateFaceMsg(faceMsg);
            }else if("30".equals(faceMsg.getOpType())){
                //删除操作
                faceMsgService.deleteFaceMsgByUserId(faceMsg);
            }
        }
        return deviceResponse;
  }

    /**
     * 下命令业务
     * @param deviceOrderRec
     * @return
     */
    @Override
    public DeviceResponse sendOrder(DeviceOrderRec deviceOrderRec) {
        Assert.hasText(deviceOrderRec.getDeviceSn(),"deviceSn is null");
        Assert.hasText(deviceOrderRec.getOrderCode(),"orderCode is null");
        //获取设备详情
        DeviceRegister deviceInfo = queryDevice(deviceOrderRec.getDeviceSn());
        //获取设备实例
        Device device = getDeviceInstance(deviceInfo);
        BaseDevice baseDevice = new BaseDevice(deviceInfo.getDeviceSn(),deviceInfo.getLoginIp(),deviceInfo.getAccount(),deviceInfo.getPassword(),deviceInfo.getPort());
        DeviceResponse deviceResponse =null;
        switch (deviceOrderRec.getOrderCode()){
            //开门指令
            case "10":
                deviceResponse = device.openGuardOrder(baseDevice);
                break;
        }
        //未匹配命令
        if(null==deviceResponse){
            deviceResponse = new DeviceResponse(false,"未找到该指令");
        }
        deviceOrderRec.setNewKeyid(deviceOrderRec.getId());
        //正确下发才保存
        if(deviceResponse.getIsSuccess()){
            deviceOrderRec.setId(null);
            deviceOrderResService.saveOrUpdateDeviceOrderRec(deviceOrderRec);
        }
        return deviceResponse;
    }

    /**
     * 下卡业务
     * @param deviceRegister
     * @param guardCardMsg
     * @return
     */
    @Override
    public DeviceResponse setCard(DeviceRegister deviceRegister, GuardCardMsg guardCardMsg) throws ParseException {
        Assert.hasText(deviceRegister.getDeviceSn(),"deviceSn is empty");
        Assert.hasText(guardCardMsg.getCardNo(),"cardNo is empty");
        //获取设备详情
        DeviceRegister deviceInfo = queryDevice(deviceRegister.getDeviceSn());
        //获取设备实例
        Device device = getDeviceInstance(deviceInfo);
        BaseDevice baseDevice = new BaseDevice(deviceInfo.getDeviceSn(),deviceInfo.getLoginIp(),deviceInfo.getAccount(),deviceInfo.getPassword(),deviceInfo.getPort());
        //卡号英文转数字
        String cardNo = guardCardMsg.getCardNo();
        for(int i=0;i<cardNo.length();i++){
            if(cardNo.charAt(i)>64){
                CharSequence str1 = String.valueOf((int)cardNo.charAt(i));
                CharSequence str2 = cardNo.substring(i,i+1);
                cardNo = cardNo.replace(str2,str1);
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //访客id
        if("40".equals(guardCardMsg.getLiveIdentity())){
            guardCardMsg.setUserId(Integer.parseInt("111"+guardCardMsg.getUserId()));
        }
//        if(guardCardMsg.getUserId()==null||guardCardMsg.getUserId()<0){
//            guardCardMsg.setUserId((int)(Math.random()*100));
//        }
        if(null==guardCardMsg.getEffStartTime()){
            guardCardMsg.setEffStartTime(new Date());
        }
        if(null==guardCardMsg.getEffEndTime()){
            guardCardMsg.setEffEndTime(sdf.parse("2029-12-31 23:59:59"));
        }
        BaseCard baseCard = new BaseCard(String.valueOf(guardCardMsg.getUserId()),guardCardMsg.getUserName(),cardNo,1,guardCardMsg.getEffStartTime(),guardCardMsg.getEffEndTime());
        DeviceResponse deviceResponse = null;
        //根据操作类型下发图片
        if("10".equals(guardCardMsg.getOpType())){
            deviceResponse = device.setCard(baseDevice, baseCard);
        }else if("20".equals(guardCardMsg.getOpType())){
            device.updateCard(baseDevice, baseCard);
        }else if("30".equals(guardCardMsg.getOpType())){
            device.delCard(baseDevice, baseCard);
        }else {
            throw new CheckedException("device-0008");
        }
         //更新下发ID
        guardCardMsg.setNewKeyid(guardCardMsg.getId());
        if(deviceResponse.getIsSuccess()){
            if("10".equals(guardCardMsg.getOpType())){
                guardCardMsg.setId(null);
                guardCardMsgService.saveOrUpdateGuardCardMsg(guardCardMsg);
            }else if("20".equals(guardCardMsg.getOpType())){
                guardCardMsgService.saveOrUpdateGuardCardMsg(guardCardMsg);
            }else if("30".equals(guardCardMsg.getOpType())){
                //删除操作
                guardCardMsgService.deleteFaceMsgByUserIdAndCardNo(guardCardMsg);
            }
        }
        return deviceResponse;
    }

    /**
     * 获取设备实例
     * @param deviceRegister
     * @return
     */
    @Override
    public Device getDeviceInstance(DeviceRegister deviceRegister){
        Assert.notNull(deviceRegister.getDeviceSupFun(),"device supType is not null");
        Assert.notNull(deviceRegister.getBrand(),"device brand is not null");
        Assert.notNull(deviceRegister.getDeviceType(),"deviceType brand is not null");
        //获取设备类型
        List<Integer> supFun = Arrays.stream(deviceRegister.getDeviceSupFun().split(",")).map(x -> Integer.valueOf(x)).collect(Collectors.toList());
        return new DefaultDeviceFactory().createDevice(Integer.valueOf(deviceRegister.getBrand()),Integer.valueOf(deviceRegister.getDeviceType()),supFun);
    }

    /**
     * 触发设备上传事件
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<DeviceRegister> deviceRegisters = registerService.queryList();
        //在线的设备才会启动监听事件
        deviceRegisters.stream().filter(x -> getDeviceInstance(x).isOnline(new BaseDevice(x.getDeviceSn(),x.getLoginIp(),x.getAccount(),x.getPassword(),x.getPort())).getIsSuccess())
                .forEach(x -> getDeviceInstance(x).captureEvent(new BaseDevice(x.getDeviceSn(),x.getLoginIp(),x.getAccount(),x.getPassword(),x.getPort())));

    }

    /**
     * 获取设备详情
     * @param deviceSn
     * @return
     */
    public DeviceRegister queryDevice(String deviceSn){
        //获取设备详情
        DeviceRegister deviceInfo = registerService.queryOneByDeviceSn(deviceSn);
        if(null==deviceInfo){
            throw new CheckedException("sys-0004");
        }
        return deviceInfo;
    }
}
