package com.herocheer.zhsq.localservice.impl.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.herocheer.zhsq.localservice.core.exception.CheckedException;
import com.herocheer.zhsq.localservice.impl.domain.entity.DeviceRegister;
import com.herocheer.zhsq.localservice.impl.mapper.DeviceRegisterMapper;
import com.herocheer.zhsq.localservice.impl.service.DeviceRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("deviceRegisterService")
public class DeviceRegisterServiceImpl implements DeviceRegisterService {

    @Autowired
    private DeviceRegisterMapper deviceRegisterMapper;



    @Override
    public void saveOrUpdateDeviceRegister(DeviceRegister deviceRegister) {
        DeviceRegister device = queryOneByDeviceSn(deviceRegister.getDeviceSn());
        if(null==device){
            deviceRegisterMapper.insert(deviceRegister);
        }else {
            deviceRegister.setId(device.getId());
            deviceRegisterMapper.updateById(deviceRegister);
        }
    }

    @Override
    public DeviceRegister queryOneByDeviceSn(String deviceSn) {
        QueryWrapper<DeviceRegister> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_sn",deviceSn);
        DeviceRegister deviceRegister = deviceRegisterMapper.selectOne(queryWrapper);
        return deviceRegister;
    }

    @Override
    public List<DeviceRegister> queryList() {
        QueryWrapper<DeviceRegister> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_status","1");
        return deviceRegisterMapper.selectList(queryWrapper);
    }

    @Override
    public void deleteDeviceRegisterByDeviceSn(DeviceRegister deviceRegister) {
        QueryWrapper<DeviceRegister> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_sn",deviceRegister.getDeviceSn());
        deviceRegisterMapper.delete(queryWrapper);
    }
}
