package com.herocheer.zhsq.localservice.core.device;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;

@Order(1)
public abstract class AbstractDeviceSdk implements DeviceSdk, ApplicationRunner {

     protected Boolean isSdkInit = false;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        sdkInit();
    }
}
