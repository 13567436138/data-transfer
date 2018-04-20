package com.youben.service;

import com.youben.base.GenericService;
import com.youben.entity.Task;

import java.util.Date;
import java.util.List;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-17
 * Time: 10:51
 */
public interface TaskService extends GenericService<Task> {
    boolean checkIfTaskExistsByMainTaskId(int mainTaskId);
    int countRecord(int mainTaskId,String recordStartTime,String recordEndTime);
    void updateStatusById(int id,int status);
    List<Task> findByMainTaskId(int mainTaskId);
    Task findLastTask(int mainTaskId);
    Date computeTaskStartTime(int taskId);
    Date computeTaskEndTime(int taskId);
}
