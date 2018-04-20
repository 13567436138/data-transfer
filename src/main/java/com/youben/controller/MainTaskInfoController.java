package com.youben.controller;

import com.youben.base.PaginateResult;
import com.youben.base.Pagination;
import com.youben.entity.DatabaseInfo;
import com.youben.entity.MainTaskInfo;
import com.youben.service.MainTaskInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-19
 * Time: 14:07
 */
@Controller
@RequestMapping("/transfer/mainTaskInfo")
public class MainTaskInfoController {
    @Autowired
    private MainTaskInfoService mainTaskInfoService;

    @RequestMapping("/list")
    public String list(){
        return "admins/transferManager/mainTaskInfo";
    }

    @RequestMapping("/list/data")
    @ResponseBody
    public PaginateResult<MainTaskInfo> listData(MainTaskInfo maintaskInfo, Pagination pagination, HttpServletRequest request){
        return mainTaskInfoService.findPage(pagination, maintaskInfo);
    }
}
