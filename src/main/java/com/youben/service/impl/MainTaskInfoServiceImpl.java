package com.youben.service.impl;

import com.youben.base.GenericMapper;
import com.youben.base.GenericServiceImpl;
import com.youben.entity.MainTaskInfo;
import com.youben.mapper.MainTaskInfoMapper;
import com.youben.service.MainTaskInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-19
 * Time: 14:04
 */
@Service
public class MainTaskInfoServiceImpl extends GenericServiceImpl<MainTaskInfo> implements MainTaskInfoService {
    private MainTaskInfoMapper mainTaskInfoMapper;
    @Autowired(required = true)
    public MainTaskInfoServiceImpl(MainTaskInfoMapper mainTaskInfoMapper) {
        super(mainTaskInfoMapper);
        this.mainTaskInfoMapper=mainTaskInfoMapper;
    }
}
