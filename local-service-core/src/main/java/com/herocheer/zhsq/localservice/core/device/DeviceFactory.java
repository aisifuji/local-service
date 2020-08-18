package com.herocheer.zhsq.localservice.core.device;

import java.util.List;

public interface DeviceFactory {
    Device createDevice(Integer brand,Integer deviceType,List<Integer> deviceSupFun);
}
