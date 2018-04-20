package com.youben.config;

import com.youben.constant.DataTransferConst;
import com.youben.entity.Task;
import com.youben.entity.TaskThread;
import com.youben.service.TaskService;
import com.youben.service.TaskThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.xml.datatype.DatatypeConfigurationException;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-20
 * Time: 15:11
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskThreadService taskThreadService;


    @Scheduled(cron = "0 0/10 * * * ?") // 每10分钟执行一次
    public void checkTaskStatus() {
        List<Task> taskList=taskService.findAll();
        for(int i=0;i<taskList.size();i++){
            Task task=taskList.get(i);
            List<TaskThread> taskThreadList=taskThreadService.findByTaskId(task.getId());
            if(taskThreadList.size()>0){
                boolean isrun=false;
                boolean isrerun=false;
                boolean isRerunFail=false;
                boolean isRunFail=false;
                boolean isRerunSuccess=false;
                for(int j=0;j<taskThreadList.size();j++){
                    TaskThread taskThread=taskThreadList.get(j);
                    if(taskThread.getStatus()== DataTransferConst.TASKTHREAD_STATUS_RERUN){
                        isrerun=true;
                        break;
                    }
                    if(taskThread.getStatus()==DataTransferConst.TASKTHREAD_STATUS_RUN){
                        isrun=true;
                    }
                    if(taskThread.getStatus()== DataTransferConst.TASKTHREAD_STATUS_FAIL){
                        isRunFail=true;
                    }
                    if(taskThread.getStatus()==DataTransferConst.TASKTHREAD_STATUS_RERUN_FAIL){
                        isRerunFail=true;
                    }

                    if(taskThread.getStatus()==DataTransferConst.TASKTHREAD_STATUS_RERUN_SUCCESS){
                        isRerunSuccess=true;
                    }
                }
                Date startDate=taskService.computeTaskStartTime(task.getId());
                Date endDate=null;
                if(isrerun){
                    taskService.updateStatusById(task.getId(),DataTransferConst.TASK_STATUS_RERUN);
                }else if(isrun){
                    taskService.updateStatusById(task.getId(),DataTransferConst.TASK_STATUS_RUN);
                }else if(isRerunFail){
                    endDate=taskService.computeTaskEndTime(task.getId());
                    taskService.updateStatusById(task.getId(),DataTransferConst.TASK_STATUS_RERUN_FAIL);
                }else if(isRunFail){
                    endDate=taskService.computeTaskEndTime(task.getId());
                    taskService.updateStatusById(task.getId(),DataTransferConst.TASK_STATUS_FAIL);
                }else{
                    endDate=taskService.computeTaskEndTime(task.getId());
                    if(isRerunSuccess){
                        taskService.updateStatusById(task.getId(),DataTransferConst.TASK_STATUS_RERUN_SUCCESS);
                    }else{
                        taskService.updateStatusById(task.getId(),DataTransferConst.TASK_STATUS_SUCCESS);
                    }
                }

                task.setStartTime(startDate);
                task.setStopTime(endDate);

                taskService.update(task);
            }
        }
    }
}
