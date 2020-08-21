package com.herocheer.zhsq.localservice.impl.schedule;

import com.herocheer.zhsq.localservice.core.device.DefaultDeviceFactory;
import com.herocheer.zhsq.localservice.core.device.entity.BaseDevice;
import com.herocheer.zhsq.localservice.core.util.FileUtil;
import com.herocheer.zhsq.localservice.impl.config.CommonProperties;
import com.herocheer.zhsq.localservice.impl.domain.entity.DeviceRegister;
import com.herocheer.zhsq.localservice.impl.service.DeviceOperationService;
import com.herocheer.zhsq.localservice.impl.service.DeviceRegisterService;
import com.herocheer.zhsq.localservice.impl.service.DeviceUploadService;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 定时上传设备活动状态
 */
@Configuration
@EnableScheduling
public class DeviceStatusSchedule {

    @Autowired
    private DeviceRegisterService deviceRegisterService;
    @Autowired
    private DeviceOperationService deviceOperationService;
    @Autowired
    private DeviceUploadService deviceUploadService;
    @Autowired
    private CommonProperties commonProperties;

    private Map<String,Boolean> deviceStatus = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(DeviceStatusSchedule.class);

    @Scheduled(cron = "0 0/5 * * * ?")
    private void uploadDeviceStatus() {
        logger.info("检测设备状态。。");
        List<DeviceRegister> deviceRegisters = deviceRegisterService.queryList();
        deviceRegisters.stream().forEach(x->{
            deviceOperationService.getDeviceStatus(x);
            //之前离线现在检测上线设备重启capture
            if(deviceStatus.containsKey(x.getDeviceSn())&&!deviceStatus.get(x.getDeviceSn())
                    &&x.getOnline()==1?true:false){
                logger.info("设备重新连接，设备序列号，{}，启动抓拍服务!",x.getDeviceSn());
                deviceOperationService.getDeviceInstance(x).captureEvent(new BaseDevice(x.getDeviceSn(),x.getLoginIp(),x.getAccount(),x.getPassword(),x.getPort()));
            }

            if(!deviceStatus.containsKey(x.getDeviceSn())){
                deviceStatus.put(x.getDeviceSn(),x.getOnline()==1?true:false);
            }
            deviceUploadService.uploadDeviceStatus(x);});
    }

    /**
     * 定时任务清除7天以前文件
     */
    @Scheduled(cron = "0 0 2 * * ?")
    private void delOldPicDirOrVideoDir(){
        logger.info("开始清除本地多余图片和视频。。。");
        doDelDir(commonProperties.getAnalysisPicPath());
        doDelDir(commonProperties.getAnalysisVideoPath());
        doDelDir(commonProperties.getDnakeCatchBackgroundPicPath());
        doDelDir(commonProperties.getDnakeCatchFacePicPath());
        doDelDir(commonProperties.getPicWhiteDownloadLocalPath());
        doDelDir(commonProperties.getPicBlackDownloadLocalPath());
        doDelDir(commonProperties.getYtCatchFacePicPath());
        doDelDir(commonProperties.getYtCatchBackgroundPicPath());
    }

    private void doDelDir(String path){
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            List<String> files = FileUtil.getFiles(path);

            files.stream().forEach(x->{
                try {
                    Date parse = df.parse(x);
                    Date nowDay = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(nowDay);
                    calendar.add(Calendar.WEEK_OF_MONTH, -1);//当前时间减去一周
                    calendar.getTime();//获取一周前时间
                    if(calendar.getTime().after(parse)){
                       File[] listFile = new File(path+x).listFiles();
                        for(File temp : listFile){
                            // 判断该temp对象是否为文件对象
                            if (temp.isFile()) {
                                temp.delete();
                            }
                        }
                    }
                } catch (ParseException e) {
                    logger.error("文件夹转换日期异常");
                    e.printStackTrace();
                }
            });
        }catch (Exception e){

        }
    }

}
