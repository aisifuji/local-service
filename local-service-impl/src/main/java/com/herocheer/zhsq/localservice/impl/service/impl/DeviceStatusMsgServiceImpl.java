package com.herocheer.zhsq.localservice.impl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.herocheer.zhsq.localservice.impl.config.CommonProperties;
import com.herocheer.zhsq.localservice.impl.domain.entity.DeviceStatusMsg;
import com.herocheer.zhsq.localservice.impl.mapper.DeviceStatusMsgMapper;
import com.herocheer.zhsq.localservice.impl.service.DeviceStatusMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("deviceStatusMsgService")
public class DeviceStatusMsgServiceImpl implements DeviceStatusMsgService {

    @Autowired
    private DeviceStatusMsgMapper deviceStatusMsgMapper;
    @Autowired
    private CommonProperties commonProperties;


    @Override
    public DeviceStatusMsg queryOneByDeviceSn(String deviceSn) {
        QueryWrapper<DeviceStatusMsg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_sn",deviceSn);
        return deviceStatusMsgMapper.selectOne(queryWrapper);
    }

    @Override
    public void saveOrUpdateDeviceStatusMsg(DeviceStatusMsg deviceStatusMsg) {
        DeviceStatusMsg device = queryOneByDeviceSn(deviceStatusMsg.getDeviceSn());
        deviceStatusMsg.setLastCheckTime(new Date());
        deviceStatusMsg.setCheckInterval(commonProperties.getDeviceCheckInterval());
        if(null==device){
            deviceStatusMsg.setDeviceBeat(1);
            deviceStatusMsgMapper.insert(deviceStatusMsg);
        }else {
            deviceStatusMsg.setId(device.getId());
            deviceStatusMsg.setDeviceBeat(device.getDeviceBeat()+1);
            deviceStatusMsgMapper.updateById(deviceStatusMsg);
        }
    }
}
