package com.herocheer.zhsq.localservice.impl.service;

import com.herocheer.zhsq.localservice.impl.domain.entity.FaceMsg;

public interface FaceMsgService {

    void saveOrUpdateFaceMsg(FaceMsg faceMsg);

    FaceMsg queryOneByUserId(Integer userId);

    void deleteFaceMsgByUserId(FaceMsg faceMsg);
}
