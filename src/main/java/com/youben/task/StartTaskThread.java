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

import javax.activation.DataContentHandler;
import javax.xml.crypto.Data;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
            ResultSet rs0 = null;

            try {
                dbmd = conn.getMetaData();
                rs0 = dbmd.getTables(null, null, null, new String[]{"TABLE"});
                while (rs0.next()) {
                    tableNameList.add(rs0.getString("TABLE_NAME"));
                }
                boolean exists = taskService.checkIfTaskExistsByMainTaskId(mainTask.getId());
                if (exists) {
                    for (int i = 0; i < tableNameList.size(); i++) {
                        String tableName = tableNameList.get(i);
                        PreparedStatement ps = null;
                        PreparedStatement ps2 = null;
                        PreparedStatement ps3 = null;
                        PreparedStatement ps5 = null;
                        ResultSet rs2=null;
                        ResultSet rs3=null;
                        ResultSet rs4=null;
                        ResultSet rs=null;
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
                                    taskThread.setStatus(DataTransferConst.TASKTHREAD_STATUS_NEW);
                                    taskThread.setSuccessCount(0);
                                    taskThread.setTableName(tableName);
                                    taskThread.setRecordStartTime(task.getRecordModifyTimeBegin());
                                    taskThread.setRecordEndTime(task.getRecordModifyTimeEnd());
                                    if(tableName.equals("sequences")) {
                                        rs4   = dbmd.getPrimaryKeys(null, null, tableName);
                                        List<String> pkList=new ArrayList<>();
                                        while (rs4.next())  {
                                            pkList.add(rs4.getString("COLUMN_NAME"));
                                        }
                                        String startPk="";
                                        String stopPk="";
                                        boolean shouldContinue=false;
                                        for(int j=0;j<pkList.size();j++) {
                                            try{
                                                ps2 = conn.prepareStatement("select  "+pkList.get(j)+"  from " + tableName + "  where last_modified>? and last_modified<=? order by last_modified  limit 1");
                                                ps2.setDate(1, new java.sql.Date(task.getRecordModifyTimeBegin().getTime()));
                                                ps2.setDate(2, new java.sql.Date(task.getRecordModifyTimeEnd().getTime()));
                                                rs2 = ps2.executeQuery();
                                                if (rs2.next()) {
                                                    startPk+=rs2.getLong(1);
                                                }else{
                                                    shouldContinue=true;
                                                    break;
                                                }
                                                ps3 = conn.prepareStatement("select "+pkList.get(j)+"  from " + tableName + "  where last_modified>? and last_modified<=? order by last_modified desc limit 1");
                                                ps3.setDate(1, new java.sql.Date(task.getRecordModifyTimeBegin().getTime()));
                                                ps3.setDate(2, new java.sql.Date(task.getRecordModifyTimeEnd().getTime()));
                                                rs3 = ps3.executeQuery();
                                                if (rs3.next()) {
                                                    stopPk+=rs3.getLong(1);
                                                }
                                            }finally {
                                                if(ps2!=null) {
                                                    ps2.close();
                                                }
                                                if(rs2!=null) {
                                                    rs2.close();
                                                }
                                                if(ps3!=null) {
                                                    ps3.close();
                                                }if(rs3!=null) {
                                                    rs3.close();
                                                }
                                            }
                                        }
                                        if(shouldContinue){
                                            continue;
                                        }
                                        taskThread.setStartRecordId(startPk);
                                        taskThread.setStopRecordId(stopPk);
                                    }else{
                                        rs4   = dbmd.getPrimaryKeys(null, null, tableName);
                                        List<String> pkList=new ArrayList<>();
                                        while (rs.next())  {
                                            pkList.add(rs4.getString("COLUMN_NAME"));
                                        }
                                        String startPk="";
                                        String stopPk="";
                                        boolean shouldContinue=false;
                                        for(int j=0;j<pkList.size();j++) {
                                            try{
                                                ps2 = conn.prepareStatement("select  "+pkList.get(j)+"  from " + tableName + "  where gmt_modified>? and gmt_modified<=? order by gmt_modified  limit 1");
                                                ps2.setDate(1, new java.sql.Date(task.getRecordModifyTimeBegin().getTime()));
                                                ps2.setDate(2, new java.sql.Date(task.getRecordModifyTimeEnd().getTime()));
                                                rs2 = ps2.executeQuery();
                                                if (rs2.next()) {
                                                    startPk+=rs2.getLong(1);
                                                }else{
                                                    shouldContinue=true;
                                                    break;
                                                }
                                                ps3 = conn.prepareStatement("select "+pkList.get(j)+"  from " + tableName + "  where gmt_modified>? and gmt_modified<=? order by gmt_modified desc limit 1");
                                                ps3.setDate(1, new java.sql.Date(task.getRecordModifyTimeBegin().getTime()));
                                                ps3.setDate(2, new java.sql.Date(task.getRecordModifyTimeEnd().getTime()));
                                                rs3 = ps3.executeQuery();
                                                if (rs3.next()) {
                                                    stopPk+=rs3.getLong(1);
                                                }
                                            }finally {
                                                if(ps2!=null) {
                                                    ps2.close();
                                                }
                                                if(rs2!=null) {
                                                    rs2.close();
                                                }
                                                if(ps3!=null) {
                                                    ps3.close();
                                                }if(rs3!=null) {
                                                    rs3.close();
                                                }
                                            }
                                        }
                                        if(shouldContinue){
                                            continue;
                                        }
                                        taskThread.setStartRecordId(startPk);
                                        taskThread.setStopRecordId(stopPk);
                                    }
                                    taskThreadService.insert(taskThread);
                                    executorService.submit(new DoTransferThread(DataTransferConst.DO_JOB_TYPE_NEW,from.getConnection(),to.getConnection(),taskThread));
                                }else{
                                    int round=needToProccessCount/DataTransferConst.COUNT_PER_THREAD;
                                    if(needToProccessCount% DataTransferConst.COUNT_PER_THREAD>0){
                                        round++;
                                    }
                                    long begin=task.getRecordModifyTimeBegin().getTime();
                                    long end=task.getRecordModifyTimeEnd().getTime();
                                    long span=(end-begin)/round;
                                    for(int j=0;j<round;j++){
                                        TaskThread taskThread=new TaskThread();
                                        long beginPerTask=begin+span*j;
                                        long endPerTask=0;
                                        if(j==round-1){
                                            endPerTask=end;
                                        }else{
                                            endPerTask=beginPerTask+span;
                                        }

                                        taskThread.setFailCount(0);
                                        taskThread.setTaskId(task.getId());
                                        taskThread.setRunCount(1);
                                        taskThread.setStatus(DataTransferConst.TASKTHREAD_STATUS_NEW);
                                        taskThread.setSuccessCount(0);
                                        taskThread.setTableName(tableName);
                                        taskThread.setRecordStartTime(new Date(beginPerTask));
                                        taskThread.setRecordEndTime(new Date(endPerTask));
                                        if(tableName.equals("sequences")) {
                                            PreparedStatement ps7 =null;
                                            ResultSet rs7=null;
                                            try {
                                                ps7 = conn.prepareStatement("select count(*) from " + tableName + "   where last_modified>? and last_modified<=?");
                                                ps7.setDate(1, new java.sql.Date(beginPerTask));
                                                ps7.setDate(2, new java.sql.Date(endPerTask));
                                                rs7 = ps7.executeQuery();
                                            }finally {
                                                ps7.close();
                                                rs7.close();
                                            }
                                            if(rs7.next()) {
                                                taskThread.setRecordCount(rs7.getInt(1));
                                            }

                                            rs4   = dbmd.getPrimaryKeys(null, null, tableName);
                                            List<String> pkList=new ArrayList<>();
                                            while (rs.next())  {
                                                pkList.add(rs4.getString("COLUMN_NAME"));
                                            }
                                            String startPk="";
                                            String stopPk="";
                                            boolean shouldContinue=false;
                                            for(int k=0;k<pkList.size();k++) {
                                                try{
                                                    ps2 = conn.prepareStatement("select  " + pkList.get(k) + "  from " + tableName + "  where last_modified>? and last_modified<=? order by last_modified  limit 1");
                                                    ps2.setDate(1, new java.sql.Date(beginPerTask));
                                                    ps2.setDate(2, new java.sql.Date(endPerTask));
                                                    rs2 = ps2.executeQuery();
                                                    if (rs2.next()) {
                                                        startPk+=rs2.getLong(1);
                                                    }else{
                                                        shouldContinue=true;
                                                        break;
                                                    }

                                                    ps3 = conn.prepareStatement("select " + pkList.get(k) + "  from " + tableName + "  where last_modified>? and last_modified<=? order by last_modified desc  limit 1");
                                                    ps3.setDate(1, new java.sql.Date(beginPerTask));
                                                    ps3.setDate(2, new java.sql.Date(endPerTask));
                                                    rs3 = ps3.executeQuery();
                                                    if (rs3.next()) {
                                                        stopPk+=rs3.getLong(1);
                                                    }
                                                }finally {
                                                    if(ps2!=null) {
                                                        ps2.close();
                                                    }
                                                    if(rs2!=null) {
                                                        rs2.close();
                                                    }
                                                    if(ps3!=null) {
                                                        ps3.close();
                                                    }if(rs3!=null) {
                                                        rs3.close();
                                                    }
                                                }
                                            }
                                            if(shouldContinue){
                                                continue;
                                            }
                                            taskThread.setStartRecordId(startPk);
                                            taskThread.setStopRecordId(stopPk);
                                        }else{
                                            PreparedStatement ps7 =null;
                                            ResultSet rs7=null;
                                            try {
                                                ps7= conn.prepareStatement("select count(*) from " + tableName + "   where gmt_modified>? and gmt_modified<=?");
                                                ps7.setDate(1, new java.sql.Date(beginPerTask));
                                                ps7.setDate(2, new java.sql.Date(endPerTask));
                                                rs7= ps7.executeQuery();
                                                if (rs7.next()) {
                                                    taskThread.setRecordCount(rs7.getInt(1));
                                                }
                                            }finally {
                                                ps7.close();
                                                rs7.close();
                                            }
                                            rs4   = dbmd.getPrimaryKeys(null, null, tableName);
                                            List<String> pkList=new ArrayList<>();
                                            while (rs4.next())  {
                                                pkList.add(rs4.getString("COLUMN_NAME"));
                                            }
                                            String startPk="";
                                            String stopPk="";
                                            boolean shouldContinue=false;
                                            for(int k=0;k<pkList.size();k++) {
                                                try {
                                                    ps2 = conn.prepareStatement("select  " + pkList.get(k) + "  from " + tableName + "  where gmt_modified>? and gmt_modified<=? order by gmt_modified  limit 1");

                                                    ps2.setDate(1, new java.sql.Date(beginPerTask));
                                                    ps2.setDate(2, new java.sql.Date(endPerTask));
                                                    rs2 = ps2.executeQuery();
                                                    if (rs2.next()) {
                                                        startPk+=rs2.getLong(1);
                                                    }else{
                                                        shouldContinue=true;
                                                        break;
                                                    }

                                                    ps3 = conn.prepareStatement("select " + pkList.get(k) + "  from " + tableName + "  where gmt_modified>? and gmt_modified<=? order by gmt_modified desc  limit 1");

                                                    ps3.setDate(1, new java.sql.Date(beginPerTask));
                                                    ps3.setDate(2, new java.sql.Date(endPerTask));
                                                    rs3 = ps3.executeQuery();
                                                    if (rs3.next()) {
                                                        stopPk+=rs3.getLong(1);
                                                    }
                                                }finally {
                                                    if(ps2!=null) {
                                                        ps2.close();
                                                    }
                                                    if(rs2!=null) {
                                                        rs2.close();
                                                    }
                                                    if(ps3!=null) {
                                                        ps3.close();
                                                    }if(rs3!=null) {
                                                        rs3.close();
                                                    }
                                                }
                                            }
                                            if(shouldContinue){
                                                continue;
                                            }
                                            taskThread.setStartRecordId(startPk);
                                            taskThread.setStopRecordId(stopPk);
                                        }
                                        taskThreadService.insert(taskThread);
                                        executorService.submit(new DoTransferThread(DataTransferConst.DO_JOB_TYPE_NEW,from.getConnection(),to.getConnection(),taskThread));
                                    }

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

                            rs4.close();
                        }
                    }
                }else{
                    for (int i = 0; i < tableNameList.size(); i++) {
                        String tableName = tableNameList.get(i);
                        PreparedStatement ps = null;
                        PreparedStatement ps2 = null;
                        PreparedStatement ps3 = null;
                        ResultSet rs2=null;
                        ResultSet rs3=null;
                        ResultSet rs4=null;
                        ResultSet rs=null;
                        try {
                            if(tableName.equals("sequences")) {
                                ps = conn.prepareStatement("select count(*) from " + tableName + "  where last_modified<=?");
                            }else{
                                ps = conn.prepareStatement("select count(*) from " + tableName + "  where  gmt_modified<=?");
                            }
                            ps.setDate(1, new java.sql.Date(task.getRecordModifyTimeEnd().getTime()));
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                int needToProccessCount = rs.getInt(1);
                                if(needToProccessCount<=DataTransferConst.COUNT_PER_THREAD){
                                    TaskThread taskThread=new TaskThread();
                                    taskThread.setFailCount(0);
                                    taskThread.setTaskId(task.getId());
                                    taskThread.setRecordCount(needToProccessCount);
                                    taskThread.setRunCount(1);
                                    taskThread.setStatus(DataTransferConst.TASKTHREAD_STATUS_NEW);
                                    taskThread.setSuccessCount(0);
                                    taskThread.setTableName(tableName);
                                    taskThread.setRecordEndTime(task.getRecordModifyTimeEnd());
                                    if(tableName.equals("sequences")) {
                                        rs4   = dbmd.getPrimaryKeys(null, null, tableName);
                                        List<String> pkList=new ArrayList<>();
                                        while (rs.next())  {
                                            pkList.add(rs4.getString("COLUMN_NAME"));
                                        }
                                        String startPk="";
                                        String stopPk="";
                                        boolean shouldContinue=false;
                                        for(int j=0;j<pkList.size();j++) {
                                            try{
                                                ps2 = conn.prepareStatement("select  "+pkList.get(j)+"  from " + tableName + "  where  last_modified<=? order by last_modified  limit 1");
                                                ps2.setDate(1, new java.sql.Date(task.getRecordModifyTimeEnd().getTime()));
                                                rs2 = ps2.executeQuery();
                                                if (rs2.next()) {
                                                    startPk+=rs2.getLong(1);
                                                }else{
                                                    shouldContinue=true;
                                                    break;
                                                }
                                                ps3 = conn.prepareStatement("select "+pkList.get(j)+"  from " + tableName + "  where last_modified>? and last_modified<=? order by last_modified desc limit 1");
                                                ps3.setDate(1, new java.sql.Date(task.getRecordModifyTimeBegin().getTime()));
                                                ps3.setDate(2, new java.sql.Date(task.getRecordModifyTimeEnd().getTime()));
                                                rs3 = ps3.executeQuery();
                                                if (rs3.next()) {
                                                    stopPk+=rs3.getLong(1);
                                                }
                                            }finally {
                                                if(ps2!=null) {
                                                    ps2.close();
                                                }
                                                if(rs2!=null) {
                                                    rs2.close();
                                                }
                                                if(ps3!=null) {
                                                    ps3.close();
                                                }if(rs3!=null) {
                                                    rs3.close();
                                                }
                                            }
                                        }
                                        if(shouldContinue){
                                            continue;
                                        }
                                        taskThread.setStartRecordId(startPk);
                                        taskThread.setStopRecordId(stopPk);
                                    }else{
                                        rs4   = dbmd.getPrimaryKeys(null, null, tableName);
                                        List<String> pkList=new ArrayList<>();
                                        while (rs.next())  {
                                            pkList.add(rs4.getString("COLUMN_NAME"));
                                        }
                                        String startPk="";
                                        String stopPk="";
                                        boolean shouldContinue=false;
                                        for(int j=0;j<pkList.size();j++) {
                                            try{
                                                ps2 = conn.prepareStatement("select  "+pkList.get(j)+"  from " + tableName + "  where  gmt_modified<=? order by gmt_modified  limit 1");
                                                ps2.setDate(1, new java.sql.Date(task.getRecordModifyTimeEnd().getTime()));
                                                rs2 = ps2.executeQuery();
                                                if (rs2.next()) {
                                                    startPk+=rs2.getLong(1);
                                                }else{
                                                    shouldContinue=true;
                                                    break;
                                                }
                                                ps3 = conn.prepareStatement("select "+pkList.get(j)+"  from " + tableName + "  where gmt_modified<=? order by gmt_modified desc limit 1");
                                                ps3.setDate(1, new java.sql.Date(task.getRecordModifyTimeEnd().getTime()));
                                                rs3 = ps3.executeQuery();
                                                if (rs3.next()) {
                                                    stopPk+=rs3.getLong(1);
                                                }
                                            }finally {
                                                if(ps2!=null) {
                                                    ps2.close();
                                                }
                                                if(rs2!=null) {
                                                    rs2.close();
                                                }
                                                if(ps3!=null) {
                                                    ps3.close();
                                                }if(rs3!=null) {
                                                    rs3.close();
                                                }
                                            }
                                        }
                                        if(shouldContinue){
                                            continue;
                                        }
                                        taskThread.setStartRecordId(startPk);
                                        taskThread.setStopRecordId(stopPk);
                                    }
                                    taskThreadService.insert(taskThread);
                                    executorService.submit(new DoTransferThread(DataTransferConst.DO_JOB_TYPE_NEW,from.getConnection(),to.getConnection(),taskThread));
                                }else{
                                    int round=needToProccessCount/DataTransferConst.COUNT_PER_THREAD;
                                    if(needToProccessCount% DataTransferConst.COUNT_PER_THREAD>0){
                                        round++;
                                    }
                                    long begin=task.getRecordModifyTimeBegin().getTime();
                                    long end=task.getRecordModifyTimeEnd().getTime();
                                    long span=(end-begin)/round;
                                    for(int j=0;j<round;j++){
                                        TaskThread taskThread=new TaskThread();
                                        long beginPerTask=begin+span*j;
                                        long endPerTask=0;
                                        if(j==round-1){
                                            endPerTask=end;
                                            taskThread.setRecordCount(needToProccessCount-DataTransferConst.COUNT_PER_THREAD*j);
                                        }else{
                                            endPerTask=beginPerTask+span;
                                            taskThread.setRecordCount(DataTransferConst.COUNT_PER_THREAD);
                                        }
                                        taskThread.setFailCount(0);
                                        taskThread.setTaskId(task.getId());
                                        taskThread.setRunCount(1);
                                        taskThread.setStatus(DataTransferConst.TASKTHREAD_STATUS_NEW);
                                        taskThread.setSuccessCount(0);
                                        taskThread.setTableName(tableName);
                                        taskThread.setRecordStartTime(new Date(beginPerTask));
                                        taskThread.setRecordEndTime(new Date(endPerTask));
                                        if(tableName.equals("sequences")) {
                                            PreparedStatement ps7 =null;
                                            ResultSet rs7=null;
                                            try {
                                                ps7 = conn.prepareStatement("select count(*) from " + tableName + "   where last_modified>? and last_modified<=?");
                                                ps7.setDate(1, new java.sql.Date(beginPerTask));
                                                ps7.setDate(2, new java.sql.Date(endPerTask));
                                                rs7 = ps7.executeQuery();
                                            }finally {
                                                ps7.close();
                                                rs7.close();
                                            }
                                            rs4   = dbmd.getPrimaryKeys(null, null, tableName);
                                            List<String> pkList=new ArrayList<>();
                                            while (rs.next())  {
                                                pkList.add(rs4.getString("COLUMN_NAME"));
                                            }
                                            String startPk="";
                                            String stopPk="";
                                            boolean shouldContinue=false;
                                            for(int k=0;k<pkList.size();k++) {
                                                try{
                                                    if(beginPerTask==begin) {
                                                        ps2 = conn.prepareStatement("select  " + pkList.get(k) + "  from " + tableName + "  where  last_modified<=? order by last_modified  limit 1");
                                                        ps2.setDate(1, new java.sql.Date(endPerTask));
                                                        rs2 = ps2.executeQuery();
                                                        if (rs2.next()) {
                                                            startPk += rs2.getLong(1);
                                                        }else{
                                                            shouldContinue=true;
                                                            break;
                                                        }
                                                        ps3 = conn.prepareStatement("select " + pkList.get(k) + "  from " + tableName + "  where last_modified<=? order by last_modified desc limit 1");
                                                        ps3.setDate(1, new java.sql.Date(endPerTask));
                                                        rs3 = ps3.executeQuery();
                                                        if (rs3.next()) {
                                                            stopPk += rs3.getLong(1);
                                                        }
                                                    }else{
                                                        ps2 = conn.prepareStatement("select  " + pkList.get(k) + "  from " + tableName + "  where last_modified>? and last_modified<=? order by last_modified  limit 1");
                                                        ps2.setDate(1, new java.sql.Date(beginPerTask));
                                                        ps2.setDate(2, new java.sql.Date(endPerTask));
                                                        rs2 = ps2.executeQuery();
                                                        if (rs2.next()) {
                                                            startPk += rs2.getLong(1);
                                                        }else{
                                                            shouldContinue=true;
                                                            break;
                                                        }

                                                        ps3 = conn.prepareStatement("select " + pkList.get(k) + "  from " + tableName + "  where last_modified>? and last_modified<=? order by last_modified desc limit 1");

                                                        ps3.setDate(1, new java.sql.Date(beginPerTask));
                                                        ps3.setDate(2, new java.sql.Date(endPerTask));
                                                        rs3 = ps3.executeQuery();
                                                        if (rs3.next()) {
                                                            stopPk += rs3.getLong(1);
                                                        }
                                                    }
                                                }finally {
                                                    if(ps2!=null) {
                                                        ps2.close();
                                                    }
                                                    if(rs2!=null) {
                                                        rs2.close();
                                                    }
                                                    if(ps3!=null) {
                                                        ps3.close();
                                                    }if(rs3!=null) {
                                                        rs3.close();
                                                    }
                                                }
                                            }
                                            if(shouldContinue){
                                                continue;
                                            }
                                            taskThread.setStartRecordId(startPk);
                                            taskThread.setStopRecordId(stopPk);
                                        }else{
                                            PreparedStatement ps7 =null;
                                            ResultSet rs7=null;
                                            try {
                                                ps7= conn.prepareStatement("select count(*) from " + tableName + "   where gmt_modified>? and gmt_modified<=?");
                                                ps7.setDate(1, new java.sql.Date(beginPerTask));
                                                ps7.setDate(2, new java.sql.Date(endPerTask));
                                                rs7= ps7.executeQuery();
                                                if (rs7.next()) {
                                                    taskThread.setRecordCount(rs7.getInt(1));
                                                }
                                            }finally {
                                                ps7.close();
                                                rs7.close();
                                            }
                                            rs4   = dbmd.getPrimaryKeys(null, null, tableName);
                                            List<String> pkList=new ArrayList<>();
                                            while (rs.next())  {
                                                pkList.add(rs4.getString("COLUMN_NAME"));
                                            }
                                            String startPk="";
                                            String stopPk="";
                                            boolean shouldContinue=false;
                                            for(int k=0;k<pkList.size();k++) {
                                                try{
                                                    if(begin==beginPerTask) {
                                                        ps2 = conn.prepareStatement("select  " + pkList.get(k) + "  from " + tableName + "  where  gmt_modified<=? order by gmt_modified  limit 1");
                                                        ps2.setDate(1, new java.sql.Date(endPerTask));
                                                        rs2 = ps2.executeQuery();
                                                        if (rs2.next()) {
                                                            startPk += rs2.getLong(1);
                                                        }else{
                                                            shouldContinue=true;
                                                            break;
                                                        }
                                                        ps3 = conn.prepareStatement("select " + pkList.get(k) + "  from " + tableName + "  where gmt_modified<=? order by gmt_modified desc limit 1");
                                                        ps3.setDate(1, new java.sql.Date(endPerTask));
                                                        rs3 = ps3.executeQuery();
                                                        if (rs3.next()) {
                                                            stopPk += rs3.getLong(1);
                                                        }
                                                    }else{
                                                        ps2 = conn.prepareStatement("select  " + pkList.get(k) + "  from " + tableName + "  where gmt_modified>? and gmt_modified<=? order by gmt_modified  limit 1");
                                                        ps2.setDate(1, new java.sql.Date(beginPerTask));
                                                        ps2.setDate(2, new java.sql.Date(endPerTask));
                                                        rs2 = ps2.executeQuery();
                                                        if (rs2.next()) {
                                                            startPk += rs2.getLong(1);
                                                        }else{
                                                            shouldContinue=true;
                                                            break;
                                                        }
                                                        ps3 = conn.prepareStatement("select " + pkList.get(k) + "  from " + tableName + "  where gmt_modified>? and gmt_modified<=? order by gmt_modified  desc limit 1");

                                                        ps3.setDate(1, new java.sql.Date(beginPerTask));
                                                        ps3.setDate(2, new java.sql.Date(endPerTask));
                                                        rs3 = ps3.executeQuery();
                                                        if (rs3.next()) {
                                                            stopPk += rs3.getLong(1);
                                                        }
                                                    }
                                                }finally {
                                                    if(ps2!=null) {
                                                        ps2.close();
                                                    }
                                                    if(rs2!=null) {
                                                        rs2.close();
                                                    }
                                                    if(ps3!=null) {
                                                        ps3.close();
                                                    }if(rs3!=null) {
                                                        rs3.close();
                                                    }
                                                }
                                            }
                                            if(shouldContinue){
                                                continue;
                                            }
                                            taskThread.setStartRecordId(startPk);
                                            taskThread.setStopRecordId(stopPk);
                                        }
                                        taskThreadService.insert(taskThread);
                                        executorService.submit(new DoTransferThread(DataTransferConst.DO_JOB_TYPE_NEW,from.getConnection(),to.getConnection(),taskThread));
                                    }

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

                            rs4.close();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    rs0.close();
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
