package com.herocheer.zhsq.localservice.core.device.HaCamera;

import com.ha.facecamera.configserver.ConfigServer;
import com.ha.facecamera.configserver.ConfigServerConfig;
import com.ha.facecamera.configserver.DataServer;
import com.ha.facecamera.configserver.DataServerConfig;
import com.herocheer.zhsq.localservice.core.device.AbstractDeviceSdk;
import com.herocheer.zhsq.localservice.core.exception.CheckedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HaSdkHandler extends AbstractDeviceSdk {
    // 配置链路
    private  ConfigServer configServer;
    // 数据链路
    private  DataServer dataServer ;

    private static final Logger logger = LoggerFactory.getLogger(HaSdkHandler.class);

    @Value("${dnake.camera.configPort}")
    private Integer configPort;
    @Value("${dnake.camera.dataPort}")
    private Integer dataPort;
    @Value("${dnake.camera.timeoutInMs}")
    private Integer timeoutInMs;
    @Value("${dnake.camera.defaultFacePicPath}")
    private String defaultFacePicPath;
    @Value("${dnake.camera.catchFacePicPath}")
    private String catchFacePicPath;
    @Value("${dnake.camera.catchBackgroundPicPath}")
    private String catchBackgroundPicPath;
    @Value("${dnake.camera.successVoice}")
    private String successVoice;

    @Override
    public Boolean sdkInit() {

        if(this.isSdkInit){
            logger.info("  ================== HaCamera-SDK已启动  ==================  ");
            return true;
        }
        configServer = new ConfigServer();
        dataServer = new DataServer();
        boolean configStatus = configServer.start(configPort, new ConfigServerConfig());
        if (configStatus) {
            logger.info("  ================== 配置链路启动成功  ==================  ");
        } else {
            logger.error("  ================== 配置链路启动失败,失败代码：" + configServer.getLastErrorCode());
            throw new CheckedException("device-0001",configServer.getLastErrorMsg());
        }

        // 数据链路
        boolean dataStatus = dataServer.start(dataPort, new DataServerConfig());
        if (dataStatus) {
            logger.info("  ================== 启动SDK服务监听   ================== ");
        } else {
            logger.error("  ================== 数据链路启动失败  ================== ");
            throw new CheckedException("device-0002",configServer.getLastErrorMsg());
        }
         return this.isSdkInit = true;
    }

    public ConfigServer getConfigServer() {
        return configServer;
    }

    public void setConfigServer(ConfigServer configServer) {
        this.configServer = configServer;
    }

    public DataServer getDataServer() {
        return dataServer;
    }

    public void setDataServer(DataServer dataServer) {
        this.dataServer = dataServer;
    }

    public Integer getTimeoutInMs() {
        return timeoutInMs;
    }

    public void setTimeoutInMs(Integer timeoutInMs) {
        this.timeoutInMs = timeoutInMs;
    }

    public String getDefaultFacePicPath() {
        return defaultFacePicPath;
    }

    public void setDefaultFacePicPath(String defaultFacePicPath) {
        this.defaultFacePicPath = defaultFacePicPath;
    }

    public String getCatchFacePicPath() {
        return catchFacePicPath;
    }

    public void setCatchFacePicPath(String catchFacePicPath) {
        this.catchFacePicPath = catchFacePicPath;
    }

    public String getCatchBackgroundPicPath() {
        return catchBackgroundPicPath;
    }

    public void setCatchBackgroundPicPath(String catchBackgroundPicPath) {
        this.catchBackgroundPicPath = catchBackgroundPicPath;
    }

    public String getSuccessVoice() {
        return successVoice;
    }

    public void setSuccessVoice(String successVoice) {
        this.successVoice = successVoice;
    }
}
