package com.herocheer.zhsq.localservice.api;



import com.herocheer.zhsq.localservice.core.device.entity.DeviceResponse;
import com.herocheer.zhsq.localservice.impl.domain.constant.CommunityInfo;
import com.herocheer.zhsq.localservice.impl.domain.entity.DeviceOrderRec;
import com.herocheer.zhsq.localservice.impl.domain.entity.DeviceRegister;
import com.herocheer.zhsq.localservice.impl.domain.entity.FaceMsg;
import com.herocheer.zhsq.localservice.impl.domain.entity.GuardCardMsg;
import com.herocheer.zhsq.localservice.impl.service.DeviceOperationService;
import com.herocheer.zhsq.localservice.impl.service.DeviceUploadService;
import com.herocheer.zhsq.localservice.impl.support.KafkaConsumerSupport;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;


@Controller
public class GetAndSendAction {


    @Autowired
    private DeviceOperationService deviceOperationService;
    @Autowired
    private DeviceUploadService deviceUploadService;

    private static final Logger logger = LoggerFactory.getLogger(GetAndSendAction.class);


    @KafkaListener(topics = "deviceInfo", groupId = "${spring.kafka.consumer.group-id}")
    public void registerDevice(ConsumerRecord<?, ?> record) {
        DeviceRegister deviceRegister = KafkaConsumerSupport.transformMessage(CommunityInfo.gatherSystemCode, record, DeviceRegister.class);
        if (deviceRegister == null) {
            return;
        }
        //设备注册
        DeviceResponse deviceResponse = null;
        try {
             deviceResponse = deviceOperationService.registerDevice(deviceRegister);
        } catch (Exception e) {
            logger.error("注册设备异常!异常信息:{}", e.getMessage());
            deviceUploadService.uploadRecEvent(null!=deviceRegister.getNewKeyid()?deviceRegister.getNewKeyid():deviceRegister.getId(),"deviceInfo",new DeviceResponse(false,e.getMessage()));
            return;
//            throw new CheckedException("device-0014",e.getMessage());
        }
        //上传注册信息
        deviceUploadService.uploadRecEvent(deviceRegister.getNewKeyid(),"deviceInfo",deviceResponse);
        //上传心跳
        deviceUploadService.uploadDeviceStatus(deviceRegister);

    }
    @KafkaListener(topics = "face",groupId = "${spring.kafka.consumer.group-id}")
    public void getFace(ConsumerRecord<?, ?> record){
        FaceMsg face = KafkaConsumerSupport.transformMessage(CommunityInfo.gatherSystemCode, record, FaceMsg.class);
        if(face==null){
            return;
        }
        DeviceRegister device = new DeviceRegister();
        device.setDeviceSn(face.getDeviceSn());
        //下发人脸
        DeviceResponse deviceResponse = null;

        try {
            deviceResponse = deviceOperationService.setFace(device,face);
        }catch (Exception e){
            logger.error("下发人脸异常，设备号：{}!异常信息:{}!",face.getDeviceSn(),e.getMessage());
            //下发人脸失败，上传失败原因
            deviceUploadService.uploadRecEvent(null!=face.getNewKeyid()?face.getNewKeyid():face.getId(),"face",new DeviceResponse(false,e.getMessage()));
            return;
//            throw new CheckedException("device-0015",e.getMessage());
        }
        //上传人脸成功详情
        deviceUploadService.uploadRecEvent(face.getNewKeyid(),"face",deviceResponse);

    }

    @KafkaListener(topics = "card",groupId = "${spring.kafka.consumer.group-id}")
    public void getCard(ConsumerRecord<?, ?> record){
        GuardCardMsg card = KafkaConsumerSupport.transformMessage(CommunityInfo.gatherSystemCode, record, GuardCardMsg.class);
        if(card==null){
            return;
        }
        DeviceRegister device = new DeviceRegister();
        device.setDeviceSn(card.getDeviceSn());
        //下发卡号
        DeviceResponse deviceResponse = null;
        try {
            deviceResponse = deviceOperationService.setCard(device,card);
        } catch (Exception e) {
            logger.error("下发卡号，设备号：{}!异常信息:{}",card.getDeviceSn(), e.getMessage());
            deviceUploadService.uploadRecEvent(null!=card.getNewKeyid()?card.getNewKeyid():card.getId(),"card",new DeviceResponse(false,e.getMessage()));
            return;
//            throw new CheckedException("device-0016",e.getMessage());
        }
        deviceUploadService.uploadRecEvent(card.getNewKeyid(),"card",deviceResponse);
    }

    @KafkaListener(topics = "order", groupId = "${spring.kafka.consumer.group-id}")
    public void getOrder(ConsumerRecord<?, ?> record) {
        DeviceOrderRec deviceOrderRec = KafkaConsumerSupport.transformMessage(CommunityInfo.gatherSystemCode, record, DeviceOrderRec.class);
        if (deviceOrderRec == null) {
            return;
        }
        //下发命令
        DeviceResponse deviceResponse = null;
        try {
            deviceResponse = deviceOperationService.sendOrder(deviceOrderRec);
        } catch (Exception e) {
            logger.error("下发命令失败，设备号：{}!异常信息:{}",deviceOrderRec.getDeviceSn(), e.getMessage());
            deviceUploadService.uploadRecEvent(null!=deviceOrderRec.getNewKeyid()?deviceOrderRec.getNewKeyid():deviceOrderRec.getId(),"order",new DeviceResponse(false,e.getMessage()));
            return;
//            throw new CheckedException("device-0017",e.getMessage());
        }
        deviceUploadService.uploadRecEvent(deviceOrderRec.getNewKeyid(),"order",deviceResponse);
    }

}
