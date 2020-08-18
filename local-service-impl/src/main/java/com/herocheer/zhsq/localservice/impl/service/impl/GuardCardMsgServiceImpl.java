package com.herocheer.zhsq.localservice.impl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.herocheer.zhsq.localservice.impl.domain.entity.GuardCardMsg;
import com.herocheer.zhsq.localservice.impl.mapper.GuardCardMsgMapper;
import com.herocheer.zhsq.localservice.impl.service.GuardCardMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("guardCardMsgService")
public class GuardCardMsgServiceImpl implements GuardCardMsgService {

    @Autowired
    private GuardCardMsgMapper guardCardMsgMapper;


    @Override
    public void saveOrUpdateGuardCardMsg(GuardCardMsg guardCardMsg) {
        GuardCardMsg guardCard = this.queryOneByUserIdAndCardNo(String.valueOf(guardCardMsg.getUserId()),guardCardMsg.getCardNo());
        if(null==guardCard){
            guardCardMsgMapper.insert(guardCardMsg);
        }else {
            guardCardMsg.setId(guardCard.getId());
            guardCardMsgMapper.updateById(guardCardMsg);
        }
    }

    @Override
    public GuardCardMsg queryOneByUserIdAndCardNo(String userId,String cardNo) {
        QueryWrapper<GuardCardMsg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("card_no",cardNo);
        return guardCardMsgMapper.selectOne(queryWrapper);
    }

    @Override
    public void deleteFaceMsgByUserIdAndCardNo(GuardCardMsg guardCardMsg) {
        QueryWrapper<GuardCardMsg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",guardCardMsg.getUserId());
        queryWrapper.eq("card_no",guardCardMsg.getCardNo());
        guardCardMsgMapper.delete(queryWrapper);
    }

    @Override
    public GuardCardMsg queryOneByDeviceSnAndCardNo(String DeviceSn, String cardNo) {
        QueryWrapper<GuardCardMsg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_sn",DeviceSn);
        queryWrapper.eq("card_no",cardNo);
        return guardCardMsgMapper.selectOne(queryWrapper);
    }
}
