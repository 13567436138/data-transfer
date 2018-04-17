package com.youben.controller;

import com.youben.base.PaginateResult;
import com.youben.base.Pagination;
import com.youben.entity.Task;
import com.youben.entity.TaskThread;
import com.youben.service.TaskThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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

    @RequestMapping("/list")
    public String list(){
        return "admins/transferManager/taskThread";
    }

    @RequestMapping("/list/data")
    @ResponseBody
    public PaginateResult<TaskThread> listData(TaskThread taskThread, Pagination pagination, HttpServletRequest request){
        return taskThreadService.findPage(pagination, taskThread);
    }
}
