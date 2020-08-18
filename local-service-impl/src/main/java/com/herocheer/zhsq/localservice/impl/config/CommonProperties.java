package com.herocheer.zhsq.localservice.impl.config;

import com.herocheer.zhsq.localservice.core.util.FileUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.File;

@Configuration
@Data
public class CommonProperties implements ApplicationListener<ContextRefreshedEvent> {


    @Value("${zhsq.cloud.loginName}")
    private String loginName;
    @Value("${zhsq.cloud.password}")
    private String password;
    @Value("${zhsq.cloud.api.getToken}")
    private String tokenApi;
    @Value("${zhsq.cloud.api.uploadDeviceStatus}")
    private String uploadDeviceStatusApi;
    @Value("${zhsq.cloud.api.uploadRecStatus}")
    private String recStatusBackApi;
    @Value("${deviceCheck.interval}")
    private Integer deviceCheckInterval;
    @Value("${pic.download.localPath.white}")
    private String picWhiteDownloadLocalPath;
    @Value("${pic.download.localPath.black}")
    private String picBlackDownloadLocalPath;
    @Value("${zhsq.cloud.api.uploadCaptureEvent}")
    private String uploadCaptureEvent;
    @Value("${zhsq.cloud.api.uploadGuardDoorEvent}")
    private String uploadGuardDoorEvent;
    @Value("${zhsq.cloud.api.uploadAnalysisEvent}")
    private String uploadAnalysisEvent;
    @Value("${analysis.picPath}")
    private String analysisPicPath;
    @Value("${analysis.videoPath}")
    private String analysisVideoPath;
    @Value("${zhsq.cloud.api.uploadStranger}")
    private String strangerUploadApi;
    @Value("${dnake.camera.catchFacePicPath}")
    private String dnakeCatchFacePicPath;
    @Value("${dnake.camera.catchBackgroundPicPath}")
    private String dnakeCatchBackgroundPicPath;
    @Value("${ytbox.catchFacePicPath}")
    private String ytCatchFacePicPath;
    @Value("${ytbox.catchBackgroundPicPath}")
    private String ytCatchBackgroundPicPath;

    private static final Logger logger = LoggerFactory.getLogger(CommonProperties.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initDir();
    }

    private void initDir() {
        logger.info("创建门禁设备抓拍头像路径：{}",dnakeCatchFacePicPath);
        FileUtil.judeDirExists(new File(dnakeCatchFacePicPath));
        logger.info("创建门禁设备抓拍头像背景路径：{}",dnakeCatchBackgroundPicPath);
        FileUtil.judeDirExists(new File(dnakeCatchBackgroundPicPath));
        logger.info("创建yt盒子设备抓拍头像路径：{}",ytCatchFacePicPath);
        FileUtil.judeDirExists(new File(ytCatchFacePicPath));
        logger.info("创建yt盒子设备抓拍头像背景路径：{}",ytCatchBackgroundPicPath);
        FileUtil.judeDirExists(new File(ytCatchBackgroundPicPath));
        logger.info("创建行文分析仪文件路径：{}",analysisPicPath);
        FileUtil.judeDirExists(new File(analysisPicPath));
        logger.info("创建行文分析仪视频路径：{}",analysisVideoPath);
        FileUtil.judeDirExists(new File(analysisVideoPath));
        logger.info("创建白名单文件夹路径：{}",picWhiteDownloadLocalPath);
        FileUtil.judeDirExists(new File(picWhiteDownloadLocalPath));
        logger.info("创建黑名单文件夹路径：{}",picBlackDownloadLocalPath);
        FileUtil.judeDirExists(new File(picBlackDownloadLocalPath));

    }
}
