package com.youben.controller;

import com.youben.base.PaginateResult;
import com.youben.base.Pagination;
import com.youben.entity.Datasource;
import com.youben.entity.JsonResult;
import com.youben.entity.MainTask;
import com.youben.entity.Menu;
import com.youben.service.MainTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-16
 * Time: 18:56
 */
@Controller
@RequestMapping("/transfer/mainTask")
public class MainTaskController {
    @Autowired
    private MainTaskService mainTaskService;

    @RequestMapping("/list")
    public String list(){
        return "admins/transferManager/mainTask";
    }

    @RequestMapping("/list/data")
    @ResponseBody
    public PaginateResult<MainTask> listData(MainTask mainTask, Pagination pagination, HttpServletRequest request){
        return mainTaskService.findPage(pagination, mainTask);
    }

    @RequestMapping("/add")
    @ResponseBody
    public JsonResult add(MainTask mainTask, BindingResult bindingResult){
        mainTaskService.insert(mainTask);
        JsonResult jsonResult=new JsonResult();
        jsonResult.setResult("ok");
        return jsonResult;
    }

}
