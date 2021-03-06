package com.youben.mapper;

import com.youben.anno.MyBatisDao;
import com.youben.base.GenericMapper;
import com.youben.entity.Task;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-17
 * Time: 10:50
 */
@MyBatisDao
public interface TaskMapper extends GenericMapper<Task> {
    int countByMainTaskId(int mainTaskId);
    void updateStatusById(@Param( "id")int id,@Param("status") int status);
    List<Task> findByMainTaskId(int mainTaskId);
    Task findLastTask(int mainTaskId);
    Date computeTaskStartTime(int taskId);
    Date computeTaskEndTime(int taskId);
}
