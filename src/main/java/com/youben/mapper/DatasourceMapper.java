package com.youben.mapper;

import com.youben.anno.MyBatisDao;
import com.youben.base.GenericMapper;
import com.youben.entity.Datasource;

import java.util.List;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-16
 * Time: 13:37
 */
@MyBatisDao
public interface DatasourceMapper extends GenericMapper<Datasource> {
    List<Datasource> queryByType(int type);
}
