package com.youben.controller;

import com.youben.base.GenericController;
import com.youben.base.PaginateResult;
import com.youben.base.Pagination;
import com.youben.entity.Datasource;
import com.youben.entity.Menu;
import com.youben.service.DatasourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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

    @RequestMapping("/delete")
    @ResponseBody
    public boolean delete(String id){
        datasourceService.delete(id);
        return true;
    }

    @RequestMapping("/add")
    @ResponseBody
    public boolean add(Datasource datasource){
        datasourceService.insert(datasource);
        return true;
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
