package com.youben.mapper;

import com.youben.anno.MyBatisDao;
import com.youben.base.GenericMapper;
import com.youben.entity.Task;
import com.youben.entity.TaskThread;

import java.util.List;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-17
 * Time: 13:03
 */
@MyBatisDao
public interface TaskThreadMapper extends GenericMapper<TaskThread>{
    List<TaskThread> findByTaskId(int taskId);
}
