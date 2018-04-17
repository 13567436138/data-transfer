package com.youben.service.impl;

import com.youben.base.GenericMapper;
import com.youben.base.GenericServiceImpl;
import com.youben.entity.Task;
import com.youben.mapper.TaskMapper;
import com.youben.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-17
 * Time: 10:52
 */
@Service
public class TaskServiceImpl extends GenericServiceImpl<Task> implements TaskService {
    private TaskMapper taskMapper;

    @Autowired(required = true)
    public TaskServiceImpl(TaskMapper taskMapper) {
        super(taskMapper);
        this.taskMapper=taskMapper;
    }
}
