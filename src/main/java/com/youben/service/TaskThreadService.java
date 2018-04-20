package com.youben.service;

import com.youben.base.GenericService;
import com.youben.entity.TaskThread;

import java.util.List;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-17
 * Time: 13:05
 */
public interface TaskThreadService extends GenericService<TaskThread> {
    List<TaskThread> findByTaskId(int taskId);
}
