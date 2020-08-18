package com.herocheer.zhsq.localservice.core.device.HIKVISON;

import com.herocheer.zhsq.localservice.core.device.AbstractDeviceSdk;
import com.herocheer.zhsq.localservice.core.device.HIKVISON.sdk.HCNetSDK;
import com.herocheer.zhsq.localservice.core.exception.CheckedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HIKSdkHandler extends AbstractDeviceSdk {

    private static Logger logger = LoggerFactory.getLogger(HIKSdkHandler.class);

    private HCNetSDK hcNetSDK;

    @Override
    public Boolean sdkInit() {
        if(this.isSdkInit){
            logger.info("  ================== HIKVSION-SDK已启动  ==================  ");
            return true;
        }
        logger.info("海康SDK init。。");
        HCNetSDK hcNetSDK = HCNetSDK.INSTANCE;
        if(!hcNetSDK.NET_DVR_Init()){
            throw new CheckedException("device-0004",hcNetSDK.NET_DVR_GetLastError());
        }
        this.hcNetSDK = hcNetSDK;
        return this.isSdkInit=true;
    }

    public HCNetSDK getHcNetSDK() {
        return hcNetSDK;
    }

    public void setHcNetSDK(HCNetSDK hcNetSDK) {
        this.hcNetSDK = hcNetSDK;
    }
}
