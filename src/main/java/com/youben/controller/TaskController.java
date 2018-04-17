package com.youben.controller;

import com.youben.base.PaginateResult;
import com.youben.base.Pagination;
import com.youben.constant.DataTransferConst;
import com.youben.entity.Datasource;
import com.youben.entity.JsonResult;
import com.youben.entity.MainTask;
import com.youben.entity.Task;
import com.youben.service.DatasourceService;
import com.youben.service.MainTaskService;
import com.youben.service.TaskService;
import com.youben.task.RestartTaskThread;
import com.youben.task.StartTaskThread;
import com.youben.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-17
 * Time: 10:54
 */
@RequestMapping("/transfer/task")
@Controller
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private MainTaskService mainTaskService;
    @Autowired
    private DatasourceService datasourceService;

    @RequestMapping("/list")
    public String list(){
        return "admins/transferManager/task";
    }

    @RequestMapping("/list/data")
    @ResponseBody
    public PaginateResult<Task> listData(Task task, Pagination pagination, HttpServletRequest request){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if(!StringUtils.isEmpty(task.getStartTimeStr())) {
                task.setStartTime(sdf.parse(task.getStartTimeStr()));
            }
            if(!StringUtils.isEmpty(task.getStopTimeStr())) {
                task.setStopTime(sdf.parse(task.getStopTimeStr()));
            }
            if(!StringUtils.isEmpty(task.getRecordModifyTimeBeginStr())) {
                task.setRecordModifyTimeBegin(sdf.parse(task.getRecordModifyTimeBeginStr()));
            }
            if(!StringUtils.isEmpty(task.getRecordModifyTimeEndStr())) {
                task.setRecordModifyTimeEnd(sdf.parse(task.getRecordModifyTimeEndStr()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return taskService.findPage(pagination, task);
    }

    @RequestMapping("/create")
    @ResponseBody
    public JsonResult createTask(int mainTaskId,String name,String recordStartTime,String recordEndTime){
        Task task=new Task();
        task.setMainTaskId(mainTaskId);
        task.setFailCount(0);
        task.setName(name);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = null;
        Date end = null;
        try {
            start = sdf.parse(recordStartTime);
            end = sdf.parse(recordEndTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //task.setRecordCount(taskService.countRecord(mainTaskId,recordStartTime,recordEndTime));
        task.setRecordCount(0);
        task.setRecordModifyTimeBegin(start);
        task.setRecordModifyTimeEnd(end);
        task.setRunCount(0);
        task.setSuccessCount(0);
        task.setStatus(DataTransferConst.TASK_STATUS_NEW);

        taskService.insert(task);

        JsonResult jsonResult=new JsonResult();
        jsonResult.setResult("ok");
        return jsonResult;
    }

    @RequestMapping("/countRecord")
    @ResponseBody
    public int countRecord(int mainTaskId,String recordStartTime,String recordEndTime){
        return taskService.countRecord(mainTaskId,recordStartTime,recordEndTime);

    }

    @RequestMapping("/enable")
    @ResponseBody
    public JsonResult enable(Task t){
        Task task=  taskService.getById(String.valueOf(t.getId()));
        JsonResult jsonResult=new JsonResult();
        if(task.getStatus()!=DataTransferConst.TASK_STATUS_NEW){
            jsonResult.setErrorMsg("任务不是处于新建状态");
            return jsonResult;
        }
        taskService.updateStatusById(t.getId(),DataTransferConst.TASK_STATUS_RUN);
        new StartTaskThread(t.getId()).start();
        jsonResult.setResult("ok");
        return jsonResult;
    }

    @RequestMapping("/reenable")
    @ResponseBody
    public JsonResult reenable(Task t){
        Task task=  taskService.getById(String.valueOf(t.getId()));
        JsonResult jsonResult=new JsonResult();
        if(task.getStatus()==DataTransferConst.TASK_STATUS_FAIL||task.getStatus()== DataTransferConst.TASK_STATUS_RERUN_FAIL){
            taskService.updateStatusById(t.getId(),DataTransferConst.TASK_STATUS_RERUN);
            new RestartTaskThread(t.getId()).start();
            jsonResult.setResult("ok");
            return jsonResult;
        }
        jsonResult.setErrorMsg("任务不是处于失败状态");
        return jsonResult;

    }
}
