package com.herocheer.zhsq.localservice.impl.service.impl;

import com.herocheer.zhsq.localservice.impl.domain.entity.GuardOpenRecordMsg;
import com.herocheer.zhsq.localservice.impl.mapper.GuardOpenRecordMsgMapper;
import com.herocheer.zhsq.localservice.impl.service.GuardOpenRecordMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service("guardOpenRecordMsgService")
public class GuardOpenRecordMsgServiceImpl implements GuardOpenRecordMsgService {

    @Autowired
    private GuardOpenRecordMsgMapper guardOpenRecordMsgMapper;

    @Override
    public void saveOrUpdateGuardOpenRecordMsg(GuardOpenRecordMsg guardOpenRecordMsg) {
        if(StringUtils.isEmpty(guardOpenRecordMsg.getId())){
            guardOpenRecordMsgMapper.insert(guardOpenRecordMsg);
        }else {
            guardOpenRecordMsgMapper.updateById(guardOpenRecordMsg);
        }
    }
}
