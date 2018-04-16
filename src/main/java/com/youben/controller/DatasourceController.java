package com.youben.controller;

import com.youben.base.GenericController;
import com.youben.base.PaginateResult;
import com.youben.base.Pagination;
import com.youben.entity.Datasource;
import com.youben.entity.JsonMessage;
import com.youben.entity.JsonResult;
import com.youben.entity.Menu;
import com.youben.service.DatasourceService;
import com.youben.utils.JdbcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;

@Controller
@RequestMapping("/datasource")
public class DatasourceController extends GenericController {
    @Autowired
    private DatasourceService datasourceService;

    @RequestMapping("/update")
    @ResponseBody
    public boolean update(Datasource datasource){
         datasourceService.update(datasource);
         return true;
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test(Datasource datasource){
        return "{\"result\":"+JdbcUtil.testOk(datasource)+"}";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public boolean delete(String id){
        datasourceService.delete(id);
        return true;
    }

    @RequestMapping("/add")
    @ResponseBody
    public JsonResult add(Datasource datasource){
        datasourceService.insert(datasource);
        JsonResult jsonResult=new JsonResult();
        jsonResult.setResult("ok");
        return jsonResult;
    }

    @RequestMapping("/list")
    public String list(){
        return "admins/system/datasource";
    }

    @RequestMapping("/list/data")
    @ResponseBody
    public PaginateResult<Datasource> listData(Datasource datasource, Pagination pagination, HttpServletRequest request){
        return datasourceService.findPage(pagination, datasource);
    }
}
