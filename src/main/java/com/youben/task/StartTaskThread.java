package com.youben.task;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.youben.entity.Datasource;
import com.youben.entity.MainTask;
import com.youben.entity.Task;
import com.youben.service.DatasourceService;
import com.youben.service.MainTaskService;
import com.youben.service.TaskService;
import com.youben.service.TaskThreadService;
import com.youben.utils.DatasourceUtils;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-17
 * Time: 16:58
 */
public class StartTaskThread extends Thread {
    private int taskId;
    private ExecutorService executorService;
    private DatasourceService datasourceService;
    private MainTaskService mainTaskService;
    private TaskService taskService;
    private TaskThreadService taskThreadService;
    public StartTaskThread(int taskId, DatasourceService datasourceService,MainTaskService mainTaskService,TaskService taskService,TaskThreadService taskThreadService){
        this.taskId=taskId;
        executorService= Executors.newFixedThreadPool(100);
        this.datasourceService=datasourceService;
        this.mainTaskService=mainTaskService;
        this.taskService=taskService;
        this.taskThreadService=taskThreadService;
    }
    @Override
    public void run() {
        Task task=taskService.getById(String.valueOf(taskId));
        MainTask mainTask=mainTaskService.getById(String.valueOf(task.getMainTaskId()));
        Datasource fromDatasource=datasourceService.getById(String.valueOf(mainTask.getFromSource()));
        Datasource toDatasource=datasourceService.getById(String.valueOf(mainTask.getToSource()));
        DruidDataSource from=DatasourceUtils.getDataSource(fromDatasource);
        DruidDataSource to=DatasourceUtils.getDataSource(toDatasource);
        try {
            DruidPooledConnection conn= from.getConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
