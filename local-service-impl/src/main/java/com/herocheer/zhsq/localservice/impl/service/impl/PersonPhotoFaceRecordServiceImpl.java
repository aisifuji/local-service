package com.herocheer.zhsq.localservice.impl.service.impl;

import com.herocheer.zhsq.localservice.impl.domain.entity.PersonPhotoFaceRecord;
import com.herocheer.zhsq.localservice.impl.mapper.PersonPhotoFaceRecordMapper;
import com.herocheer.zhsq.localservice.impl.service.PersonPhotoFaceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service("personPhotoFaceRecordService")
public class PersonPhotoFaceRecordServiceImpl implements PersonPhotoFaceRecordService {
    @Autowired
    private PersonPhotoFaceRecordMapper personPhotoFaceRecordMapper;

    @Override
    public void saveOrUpdatePersonPhotoFaceRecord(PersonPhotoFaceRecord personPhotoFaceRecord) {
        if(StringUtils.isEmpty(personPhotoFaceRecord.getId())){
            personPhotoFaceRecordMapper.insert(personPhotoFaceRecord);
        }else {
            personPhotoFaceRecordMapper.updateById(personPhotoFaceRecord);
        }
    }
}
