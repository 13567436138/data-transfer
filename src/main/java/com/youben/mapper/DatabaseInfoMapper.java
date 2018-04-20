package com.youben.mapper;

import com.youben.anno.MyBatisDao;
import com.youben.base.GenericMapper;
import com.youben.entity.DatabaseInfo;
import org.apache.ibatis.annotations.Param;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-19
 * Time: 14:00
 */
@MyBatisDao
public interface DatabaseInfoMapper extends GenericMapper<DatabaseInfo> {
    DatabaseInfo getByTableName(@Param("sourceId") int sourceId,@Param("tableName") String tableName);
    void replace(DatabaseInfo databaseInfo);
}
