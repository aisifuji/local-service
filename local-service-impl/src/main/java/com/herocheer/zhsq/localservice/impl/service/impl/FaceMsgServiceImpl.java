package com.herocheer.zhsq.localservice.impl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.herocheer.zhsq.localservice.impl.domain.entity.FaceMsg;
import com.herocheer.zhsq.localservice.impl.mapper.FaceMsgMapper;
import com.herocheer.zhsq.localservice.impl.service.FaceMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("faceMsgService")
public class FaceMsgServiceImpl implements FaceMsgService {

    @Autowired
    private FaceMsgMapper faceMsgMapper;

    @Override
    public void saveOrUpdateFaceMsg(FaceMsg faceMsg) {
        FaceMsg face = this.queryOneByUserId(faceMsg.getUserId());
        if(face==null){
            faceMsgMapper.insert(faceMsg);
        }else {
            faceMsg.setId(face.getId());
            faceMsgMapper.updateById(faceMsg);
        }
    }

    @Override
    public FaceMsg queryOneByUserId(Integer userId) {
        QueryWrapper<FaceMsg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        return   faceMsgMapper.selectOne(queryWrapper);
    }

    @Override
    public void deleteFaceMsgByUserId(FaceMsg faceMsg) {
        QueryWrapper<FaceMsg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",faceMsg.getDeviceSn());
        faceMsgMapper.delete(queryWrapper);
    }
}
