package com.youben.controller;

import com.alibaba.druid.pool.DruidDataSource;
import com.youben.base.PaginateResult;
import com.youben.base.Pagination;
import com.youben.constant.DataTransferConst;
import com.youben.entity.*;
import com.youben.service.DatasourceService;
import com.youben.service.MainTaskService;
import com.youben.service.TaskService;
import com.youben.service.TaskThreadService;
import com.youben.task.DoTransferThread;
import com.youben.utils.DatasourceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-17
 * Time: 13:09
 */
@RequestMapping("/transfer/thread")
@Controller
public class TaskThreadController {
    @Autowired
    private TaskThreadService taskThreadService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private MainTaskService mainTaskService;
    @Autowired
    private DatasourceService datasourceService;
    private ExecutorService executorService= Executors.newFixedThreadPool(100);


    @RequestMapping("/list")
    public String list(){
        return "admins/transferManager/taskThread";
    }

    @RequestMapping("/list/data")
    @ResponseBody
    public PaginateResult<TaskThread> listData(TaskThread taskThread, Pagination pagination, HttpServletRequest request){
        return taskThreadService.findPage(pagination, taskThread);
    }

    @ResponseBody
    @RequestMapping("/redo")
    public JsonResult redo(int id){
        TaskThread taskThread=taskThreadService.getById(String.valueOf(id));
        Task task=taskService.getById(String.valueOf(taskThread.getTaskId()));
        MainTask mainTask=mainTaskService.getById(String.valueOf(task.getMainTaskId()));
        Datasource fromDatasource=datasourceService.getById(String.valueOf(mainTask.getFromSource()));
        Datasource toDatasource=datasourceService.getById(String.valueOf(mainTask.getToSource()));
        DruidDataSource from= DatasourceUtils.getDataSource(fromDatasource);
        DruidDataSource to=DatasourceUtils.getDataSource(toDatasource);

        executorService.submit(new DoTransferThread(DataTransferConst.DO_JOB_TYPE_REDO,from,to,taskThread,taskThreadService));

        JsonResult jsonResult=new JsonResult();
        jsonResult.setResult("ok");
        return jsonResult;
    }
}
