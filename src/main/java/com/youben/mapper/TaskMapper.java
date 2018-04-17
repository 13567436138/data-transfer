package com.youben.mapper;

import com.youben.anno.MyBatisDao;
import com.youben.base.GenericMapper;
import com.youben.entity.Task;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-17
 * Time: 10:50
 */
@MyBatisDao
public interface TaskMapper extends GenericMapper<Task> {
}
