package com.youben.service.impl;

import com.youben.base.GenericMapper;
import com.youben.base.GenericServiceImpl;
import com.youben.entity.TaskThread;
import com.youben.mapper.TaskThreadMapper;
import com.youben.service.TaskThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-17
 * Time: 13:06
 */
@Service
public class TaskThreadServiceImpl extends GenericServiceImpl<TaskThread> implements TaskThreadService {
    private TaskThreadMapper taskThreadMapper;

    @Autowired(required = true)
    public TaskThreadServiceImpl(TaskThreadMapper taskThreadMapper) {
        super(taskThreadMapper);
        this.taskThreadMapper=taskThreadMapper;
    }

    @Override
    public List<TaskThread> findByTaskId(int taskId) {
        return taskThreadMapper.findByTaskId(taskId);
    }
}
