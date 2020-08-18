package com.herocheer.zhsq.localservice.core.device;

import com.herocheer.zhsq.localservice.core.exception.CheckedException;
import com.herocheer.zhsq.localservice.core.util.SpringUtil;

import java.util.*;

public class DefaultDeviceFactory implements DeviceFactory {

    @Override
    public Device createDevice(Integer brand,Integer deviceType,List<Integer> deviceSupFun) {
        Map<String, Device> beans = SpringUtil.getBeansOfType(Device.class);
        Optional<Map.Entry<String, Device>> collect = beans.entrySet().stream()
                .filter(entry->brand==entry.getValue().getBrand()&&deviceType==entry.getValue().getDeviceType() && deviceSupFun.stream()
                        .allMatch(x -> (entry.getValue().getDeviceSupFun().stream().anyMatch(y -> x == y))))
                            .findFirst();
        if(null==collect.get()){
           throw new CheckedException("sys-0001");
       }
      return collect.get().getValue();
    }

}
