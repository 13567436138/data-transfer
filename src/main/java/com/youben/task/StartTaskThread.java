package com.youben.task;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.youben.constant.DataTransferConst;
import com.youben.entity.Datasource;
import com.youben.entity.MainTask;
import com.youben.entity.Task;
import com.youben.entity.TaskThread;
import com.youben.service.DatasourceService;
import com.youben.service.MainTaskService;
import com.youben.service.TaskService;
import com.youben.service.TaskThreadService;
import com.youben.utils.DatasourceUtils;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-17
 * Time: 16:58
 */
public class StartTaskThread extends Thread {
    private int taskId;
    private ExecutorService executorService;
    private DatasourceService datasourceService;
    private MainTaskService mainTaskService;
    private TaskService taskService;
    private TaskThreadService taskThreadService;
    public StartTaskThread(int taskId, DatasourceService datasourceService,MainTaskService mainTaskService,TaskService taskService,TaskThreadService taskThreadService){
        this.taskId=taskId;
        executorService= Executors.newFixedThreadPool(100);
        this.datasourceService=datasourceService;
        this.mainTaskService=mainTaskService;
        this.taskService=taskService;
        this.taskThreadService=taskThreadService;
    }
    @Override
    public void run() {
        Task task=taskService.getById(String.valueOf(taskId));
        MainTask mainTask=mainTaskService.getById(String.valueOf(task.getMainTaskId()));
        Datasource fromDatasource=datasourceService.getById(String.valueOf(mainTask.getFromSource()));
        Datasource toDatasource=datasourceService.getById(String.valueOf(mainTask.getToSource()));
        DruidDataSource from=DatasourceUtils.getDataSource(fromDatasource);
        DruidDataSource to=DatasourceUtils.getDataSource(toDatasource);
        DruidPooledConnection conn=null;
        try {
            conn= from.getConnection();
            List<String> tableNameList=new ArrayList<String>();
            DatabaseMetaData dbmd = null;
            ResultSet rs = null;

            try {
                dbmd = conn.getMetaData();
                rs = dbmd.getTables(null, null, null, new String[]{"TABLE"});
                while (rs.next()) {
                    tableNameList.add(rs.getString("TABLE_NAME"));
                }
                boolean exists = taskService.checkIfTaskExistsByMainTaskId(mainTask.getId());
                if (exists) {
                    for (int i = 0; i < tableNameList.size(); i++) {
                        String tableName = tableNameList.get(i);
                        PreparedStatement ps = null;
                        PreparedStatement ps2 = null;
                        PreparedStatement ps3 = null;
                        ResultSet rs2=null;
                        ResultSet rs3=null;
                        try {
                            if(tableName.equals("sequences")) {
                                ps = conn.prepareStatement("select count(*) from " + tableName + "  where last_modified>? and last_modified<=?");
                            }else{
                                ps = conn.prepareStatement("select count(*) from " + tableName + "  where gmt_modified>? and gmt_modified<=?");
                            }
                            ps.setDate(1, new java.sql.Date(task.getRecordModifyTimeBegin().getTime()));
                            ps.setDate(2, new java.sql.Date(task.getRecordModifyTimeEnd().getTime()));
                            rs = ps.executeQuery();
                            if (rs.next()) {
                               int needToProccessCount = rs.getInt(1);
                                if(needToProccessCount<=DataTransferConst.COUNT_PER_THREAD){
                                    TaskThread taskThread=new TaskThread();
                                    taskThread.setFailCount(0);
                                    taskThread.setTaskId(task.getId());
                                    taskThread.setRecordCount(needToProccessCount);
                                    taskThread.setRunCount(1);
                                    taskThread.setStatus(DataTransferConst.);
                                    taskThread.setSuccessCount(0);
                                    taskThread.setTableName(tableName);
                                    if(tableName.equals("sequences")) {
                                        ps2 = conn.prepareStatement("select id from " + tableName+"  where last_modified>? and last_modified<=? order by last_modified  limit 1");
                                        rs2= ps2.executeQuery();
                                        if(rs2.next()){
                                            taskThread.setStartRecordId(String.valueOf(rs2.getLong(1)));
                                        }
                                        ps3 = conn.prepareStatement("select id from " + tableName+"  where last_modified>? and last_modified<=? order by last_modified  limit "+(needToProccessCount-1)+",1");
                                        rs3= ps3.executeQuery();
                                        if(rs3.next()){
                                            taskThread.setStopRecordId(String.valueOf(rs3.getLong(1)));
                                        }
                                    }
                                    taskThreadService.insert(taskThread);
                                    //TODO 返回id
                                    executorService.submit(new DoTransferThread(DataTransferConst.DO_JOB_TYPE_NEW,from.getConnection(),to.getConnection(),taskThread));
                                }else{

                                }

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
                            ps2.close();
                            rs2.close();
                            ps3.close();
                            rs3.close();
                        }
                    }
                }else{
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
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
