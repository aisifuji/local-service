package com.herocheer.zhsq.localservice.impl.service;

import com.herocheer.zhsq.localservice.impl.domain.entity.GuardCardMsg;

public interface GuardCardMsgService {
    void saveOrUpdateGuardCardMsg(GuardCardMsg guardCardMsg);

    GuardCardMsg queryOneByUserIdAndCardNo(String userId,String cardNo);

    void deleteFaceMsgByUserIdAndCardNo(GuardCardMsg guardCardMsg);

    GuardCardMsg queryOneByDeviceSnAndCardNo(String DeviceSn,String cardNo);
}
