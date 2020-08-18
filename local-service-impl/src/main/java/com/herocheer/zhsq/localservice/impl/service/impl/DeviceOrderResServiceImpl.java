package com.herocheer.zhsq.localservice.impl.service.impl;

import com.herocheer.zhsq.localservice.impl.domain.entity.DeviceOrderRec;
import com.herocheer.zhsq.localservice.impl.mapper.DeviceOrderRecMapper;
import com.herocheer.zhsq.localservice.impl.service.DeviceOrderResService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("deviceOrderResService")
public class DeviceOrderResServiceImpl implements DeviceOrderResService {

    @Autowired
    private DeviceOrderRecMapper deviceOrderRecMapper;

    @Override
    public void saveOrUpdateDeviceOrderRec(DeviceOrderRec deviceOrderRec) {
           if(null!=deviceOrderRec.getId()){
               deviceOrderRecMapper.updateById(deviceOrderRec);
           }else {
               deviceOrderRecMapper.insert(deviceOrderRec);
           }
    }

}
