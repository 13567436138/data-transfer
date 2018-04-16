package com.youben.service.impl;

import com.youben.base.GenericMapper;
import com.youben.base.GenericServiceImpl;
import com.youben.entity.Datasource;
import com.youben.mapper.DatasourceMapper;
import com.youben.mapper.MenuMapper;
import com.youben.service.DatasourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Description:
 * User: hxp
 * Date: 2018-04-16
 * Time: 13:32
 */
@Service
public class DatasourceServiceImpl extends GenericServiceImpl<Datasource> implements DatasourceService {
    private DatasourceMapper datasourceMapper;

    @Autowired(required = true)
    public DatasourceServiceImpl( DatasourceMapper datasourceMapper) {
        super(datasourceMapper);
        this.datasourceMapper=datasourceMapper;
    }
}
