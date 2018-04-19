package com.youben.service.impl;

import com.youben.base.GenericMapper;
import com.youben.base.GenericServiceImpl;
import com.youben.entity.DatabaseInfo;
import com.youben.mapper.DatabaseInfoMapper;
import com.youben.service.DatabaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-19
 * Time: 14:02
 */
@Service
public class DatabaseInfoServiceImpl extends GenericServiceImpl<DatabaseInfo> implements DatabaseInfoService {
    private DatabaseInfoMapper databaseInfoMapper;

    @Autowired(required = true)
    public DatabaseInfoServiceImpl(DatabaseInfoMapper databaseInfoMapper) {
        super(databaseInfoMapper);
        this.databaseInfoMapper=databaseInfoMapper;
    }
}
