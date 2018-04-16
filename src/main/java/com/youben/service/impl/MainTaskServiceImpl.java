package com.youben.service.impl;

import com.youben.base.GenericMapper;
import com.youben.base.GenericService;
import com.youben.base.GenericServiceImpl;
import com.youben.entity.MainTask;
import com.youben.mapper.MainTaskMapper;
import com.youben.service.MainTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-16
 * Time: 18:53
 */
@Service
public class MainTaskServiceImpl extends GenericServiceImpl<MainTask> implements MainTaskService {
    private MainTaskMapper mainTaskMapper;

    @Autowired(required = true)
    public MainTaskServiceImpl(MainTaskMapper dao) {
        super(dao);
        this.mainTaskMapper=dao;
    }

}
