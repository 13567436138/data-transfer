package com.youben.mapper;

import com.youben.anno.MyBatisDao;
import com.youben.base.GenericMapper;
import com.youben.entity.MainTaskInfo;
import org.apache.ibatis.annotations.Param;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-19
 * Time: 14:00
 */
@MyBatisDao
public interface MainTaskInfoMapper extends GenericMapper<MainTaskInfo> {
    MainTaskInfo getByTableName(@Param("mainTaskId")int mainTaskId,@Param("tableName") String tableName);
}
