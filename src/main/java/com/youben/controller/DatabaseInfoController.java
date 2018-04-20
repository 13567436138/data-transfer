package com.youben.controller;

import com.youben.base.PaginateResult;
import com.youben.base.Pagination;
import com.youben.entity.DatabaseInfo;
import com.youben.entity.Datasource;
import com.youben.service.DatabaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-19
 * Time: 14:06
 */
@RequestMapping("/transfer/databaseInfo")
@Controller
public class DatabaseInfoController {
    @Autowired
    private DatabaseInfoService databaseInfoService;

    @RequestMapping("/list")
    public String list(){
        return "admins/transferManager/databaseInfo";
    }

    @RequestMapping("/list/data")
    @ResponseBody
    public PaginateResult<DatabaseInfo> listData(DatabaseInfo databaseInfo, Pagination pagination, HttpServletRequest request){
        return databaseInfoService.findPage(pagination, databaseInfo);
    }
}
