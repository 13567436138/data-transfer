package com.youben.service.impl;

import com.youben.base.GenericMapper;
import com.youben.base.GenericServiceImpl;
import com.youben.entity.Datasource;
import com.youben.entity.MainTask;
import com.youben.entity.Task;
import com.youben.mapper.DatasourceMapper;
import com.youben.mapper.MainTaskMapper;
import com.youben.mapper.TaskMapper;
import com.youben.service.TaskService;
import com.youben.utils.JdbcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-17
 * Time: 10:52
 */
@Service
public class TaskServiceImpl extends GenericServiceImpl<Task> implements TaskService {
    private TaskMapper taskMapper;

    @Autowired
    private MainTaskMapper mainTaskMapper;
    @Autowired
    private DatasourceMapper datasourceMapper;

    @Autowired(required = true)
    public TaskServiceImpl(TaskMapper taskMapper) {
        super(taskMapper);
        this.taskMapper=taskMapper;
    }

    @Override
    public boolean checkIfTaskExistsByMainTaskId(int mainTaskId) {
        int count=taskMapper.countByMainTaskId(mainTaskId);
        if(count>0){
            return true;
        }
        return false;
    }

    @Override
    public int countRecord(int mainTaskId, String recordStartTime, String recordEndTime) {
        MainTask mainTask=mainTaskMapper.selectByPrimaryKey(String.valueOf(mainTaskId));
        Datasource datasource=datasourceMapper.selectByPrimaryKey(String.valueOf(mainTask.getFromSource()));
        int count=taskMapper.countByMainTaskId(mainTaskId);
        int totalCount = 0;

        DatabaseMetaData dbmd = null;
        ResultSet rs = null;
        Connection connection = JdbcUtil.getConnection(datasource);
        List<String> tableNameList=new ArrayList<String>();
        try {
            try {
                dbmd = connection.getMetaData();
                rs = dbmd.getTables(null, null, null, new String[]{"TABLE"});
                while (rs.next()) {
                    tableNameList.add(rs.getString("TABLE_NAME"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = null;
            Date end = null;
            try {
                start = sdf.parse(recordStartTime);
                end = sdf.parse(recordEndTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (count > 0) {
                for (int i = 0; i < tableNameList.size(); i++) {
                    String tableName = tableNameList.get(i);
                    PreparedStatement ps = null;
                    try {
                        if(tableName.equals("sequences")) {
                            ps = connection.prepareStatement("select count(*) from " + tableName + " where last_modified>? and last_modified<=?");
                        }else{
                            ps = connection.prepareStatement("select count(*) from " + tableName + " where gmt_modified>? and gmt_modified<=?");
                        }
                        ps.setDate(1, new java.sql.Date(start.getTime()));
                        ps.setDate(2, new java.sql.Date(end.getTime()));
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            totalCount += rs.getInt(1);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            rs.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        try {
                            ps.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                for (int i = 0; i < tableNameList.size(); i++) {
                    String tableName = tableNameList.get(i);
                    PreparedStatement ps=null;
                    try {
                        if(tableName.equals("sequences")) {
                            ps = connection.prepareStatement("select count(*) from " + tableName + " where last_modified<=?");
                        }else{
                            ps = connection.prepareStatement("select count(*) from " + tableName + " where gmt_modified<=?");
                        }
                        ps.setDate(1, new java.sql.Date(end.getTime()));
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            totalCount += rs.getInt(1);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            rs.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        try {
                            ps.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }finally{
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return totalCount;
    }

    @Override
    public void updateStatusById(int id, int status) {
        taskMapper.updateStatusById(id,status);
    }

    @Override
    public List<Task> findByMainTaskId(int mainTaskId) {
        return taskMapper.findByMainTaskId(mainTaskId);
    }

    @Override
    public Task findLastTask(int mainTaskId) {
        return taskMapper.findLastTask(mainTaskId);
    }

    @Override
    public Date computeTaskStartTime(int taskId) {
        return taskMapper.computeTaskStartTime(taskId);
    }

    @Override
    public Date computeTaskEndTime(int taskId) {
        return taskMapper.computeTaskEndTime(taskId);
    }
}
