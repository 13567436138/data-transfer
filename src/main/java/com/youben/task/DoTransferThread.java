package com.youben.task;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.youben.constant.DataTransferConst;
import com.youben.entity.TaskThread;
import com.youben.service.TaskThreadService;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-17
 * Time: 20:07
 */
public class DoTransferThread extends Thread {
    private int type;//1新的，2重做
    private DruidDataSource from;
    private DruidDataSource to;
    private TaskThread taskThread;
    private TaskThreadService taskThreadService;
    private Date fromDate;
    private List<Integer> typeList=new ArrayList<>();
    private List<String> columnNameList=new ArrayList<>();

    public DoTransferThread(int type,DruidDataSource from,DruidDataSource to,TaskThread taskThread,TaskThreadService taskThreadService){
        this.type=type;
        this.from=from;
        this.to=to;
        this.taskThread=taskThread;
        this.taskThreadService=taskThreadService;
    }
    @Override
    public void run() {
        DruidPooledConnection fromConn = null;
        DruidPooledConnection toConn=null;
        ResultSet colRet=null;

        int successCount=0;

        try {
            fromConn =from.getConnection();
            toConn=to.getConnection();
            //使用脚本建表
            //JdbcUtil.createDestTables(fromConn,toConn);

            if(type== DataTransferConst.DO_JOB_TYPE_NEW){
                taskThread.setStartTime(new Date());
                taskThread.setStatus(DataTransferConst.TASKTHREAD_STATUS_RUN);
                taskThread.setName(this.getName());
                taskThreadService.update(taskThread);

                String tableName=taskThread.getTableName();

                DatabaseMetaData fromMetaData=fromConn.getMetaData();
                colRet = fromMetaData.getColumns(null, "%", tableName, "%");
                dealColRet(colRet);

                int round=taskThread.getRecordCount()/DataTransferConst.COUNT_PER_QUERY;
                if(taskThread.getRecordCount()%DataTransferConst.COUNT_PER_QUERY!=0){
                    round++;
                }
                fromDate=taskThread.getRecordStartTime();
                for(int i=0;i<round;i++) {
                        if (tableName.equals("sequences")) {
                            successCount+=insert(fromConn,toConn,tableName,"last_modified");

                        } else {
                            successCount+= insert(fromConn, toConn, tableName, "gmt_modified");
                        }
                }

            }else if(type==DataTransferConst.DO_JOB_TYPE_REDO){
                taskThread.setStartTime(new Date());
                taskThread.setName(this.getName());
                taskThread.setRunCount(taskThread.getRunCount()+1);
                taskThread.setStatus(DataTransferConst.TASKTHREAD_STATUS_RERUN);
                taskThreadService.update(taskThread);

                String tableName=taskThread.getTableName();

                DatabaseMetaData fromMetaData=fromConn.getMetaData();
                colRet = fromMetaData.getColumns(null, "%", tableName, "%");
                dealColRet(colRet);

                int round=taskThread.getRecordCount()/DataTransferConst.COUNT_PER_QUERY;
                if(taskThread.getRecordCount()%DataTransferConst.COUNT_PER_QUERY!=0){
                    round++;
                }
                fromDate=taskThread.getRecordStartTime();
                for(int i=0;i<round;i++) {
                    if (tableName.equals("sequences")) {
                        successCount+=insert(fromConn,toConn,tableName,"last_modified");
                    } else {
                        successCount+= insert(fromConn, toConn, tableName, "gmt_modified");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fromConn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                toConn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(colRet!=null) {
                    colRet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            taskThread.setStopTime(new Date());
            taskThread.setSuccessCount(successCount);
            if(type==DataTransferConst.DO_JOB_TYPE_NEW){
                if(taskThread.getRecordCount()==successCount){
                    taskThread.setStatus(DataTransferConst.TASKTHREAD_STATUS_SUCCESS);
                }else{
                    taskThread.setStatus(DataTransferConst.TASKTHREAD_STATUS_FAIL);
                }
            }else{
                if(taskThread.getRecordCount()==successCount){
                    taskThread.setStatus(DataTransferConst.TASKTHREAD_STATUS_RERUN_SUCCESS);
                }else{
                    taskThread.setStatus(DataTransferConst.TASKTHREAD_STATUS_RERUN_FAIL);
                }
            }
            taskThreadService.update(taskThread);
        }

    }

    private void dealColRet(ResultSet colRet) throws SQLException {
        while (colRet.next()) {
            String columnName = colRet.getString("COLUMN_NAME");
            columnNameList.add(columnName);
            int type = colRet.getInt("DATA_TYPE");
            typeList.add(type);
        }
    }

    private int insert(DruidPooledConnection fromConn,DruidPooledConnection toConn,String tableName,String modifyField){
        String insertSql="replace  into "+tableName+"(";
        String valuesSql="values";
        PreparedStatement ps1=null;
        PreparedStatement ps2=null;
        PreparedStatement ps3=null;
        PreparedStatement ps4=null;
        ResultSet rs1=null;
        ResultSet rs2=null;
        ResultSet rs3=null;
        int successCount=0;
        try{
            ps1=fromConn.prepareStatement("select "+modifyField+" from " + tableName + " where "+modifyField+">? order by "+modifyField+" limit " + DataTransferConst.COUNT_PER_QUERY + ",1");
            ps1.setTimestamp(1,new java.sql.Timestamp(fromDate.getTime()));
            rs1=ps1.executeQuery();
            Date toDate=null;
            if(rs1.next()){
                toDate = rs1.getTimestamp(1);
            }
            if(toDate==null){
                toDate=taskThread.getRecordEndTime();
            }
            if(toDate.getTime()>taskThread.getRecordEndTime().getTime()){
                toDate=taskThread.getRecordEndTime();
            }
            ps2=fromConn.prepareStatement("select * from " + tableName + " where "+modifyField+">? and "+modifyField+"<=?  ");
            ps2.setTimestamp(1,new java.sql.Timestamp(fromDate.getTime()));
            ps2.setTimestamp(2,new java.sql.Timestamp(toDate.getTime()));
            rs2=ps2.executeQuery();

            for(int j=0;j<columnNameList.size();j++){
                insertSql += columnNameList.get(j) + ",";
            }

            insertSql=insertSql.substring(0,insertSql.length()-1);
            insertSql+=")";

            ps3=fromConn.prepareStatement("select count(*) from " + tableName + " where "+modifyField+">? and "+modifyField+"<=?  ");
            ps3.setTimestamp(1,new java.sql.Timestamp(fromDate.getTime()));
            ps3.setTimestamp(2,new java.sql.Timestamp(toDate.getTime()));
            rs3=ps3.executeQuery();
            if(rs3.next()){
                int count=rs3.getInt(1);
                if(count==0){
                    return successCount;
                }
                String singleValue="(";
                for(int k=0;k<typeList.size();k++){
                    singleValue+="?,";
                }
                singleValue=singleValue.substring(0,singleValue.length()-1);
                singleValue+=")";
                for(int k=0;k<count;k++){
                    valuesSql+=singleValue+",";
                }
                valuesSql=valuesSql.substring(0,valuesSql.length()-1);
                successCount+=count;
            }

            insertSql+=valuesSql;

            ps4=toConn.prepareStatement(insertSql);

            int index=1;
            while(rs2.next()){
                for(int j=0;j<typeList.size();j++){
                    setParamter(ps4,typeList.get(j),index,rs2,j+1);
                    index++;
                }
            }

            ps4.executeUpdate();

            fromDate=toDate;

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(ps1!=null){
                try {
                    ps1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(ps2!=null){
                try {
                    ps2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(rs1!=null){
                try {
                    rs1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(rs2!=null){
                try {
                    rs2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(ps3!=null){
                try {
                    ps3.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(rs3!=null){
                try {
                    rs3.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(ps4!=null){
                try {
                    ps4.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return successCount;
        }
    }


    private void setParamter(PreparedStatement ps4,int type,int index,ResultSet rs2,int j) throws SQLException {
        switch (type){
            case Types.BOOLEAN:
                ps4.setBoolean(index,rs2.getBoolean(j));
                break;
            case Types.BIT:
                ps4.setByte(index,rs2.getByte(j));
                break;
            case Types.BIGINT:
                ps4.setLong(index,rs2.getLong(j));
                break;
            case Types.INTEGER:
                ps4.setInt(index,rs2.getInt(j));
                break;
            case Types.CHAR:
                ps4.setString(index,rs2.getString(j));
                break;
            case Types.TINYINT:
                ps4.setByte(index,rs2.getByte(j));
                break;
            case Types.DATE:
                ps4.setDate(index,rs2.getDate(j));
                break;
            case Types.DECIMAL:
                ps4.setBigDecimal(index,rs2.getBigDecimal(j));
                break;
            case Types.DOUBLE:
                ps4.setDouble(index,rs2.getDouble(j));
                break;
            case Types.FLOAT:
                ps4.setDouble(index,rs2.getFloat(j));
                break;
            case Types.JAVA_OBJECT:
                ps4.setObject(index,rs2.getObject(j));
                break;
            case Types.LONGNVARCHAR:
                ps4.setString(index,rs2.getString(j));
                break;
            case Types.LONGVARBINARY:
                ps4.setBytes(index,rs2.getBytes(j));
                break;
            case Types.LONGVARCHAR:
                ps4.setString(index,rs2.getString(j));
                break;
            case Types.NCHAR:
                ps4.setString(index,rs2.getString(j));
                break;
            case Types.NCLOB:
                ps4.setBytes(index,rs2.getBytes(j));
                break;
            case Types.NULL:
                ps4.setObject(index,rs2.getObject(j));
                break;
            case Types.NUMERIC:
                ps4.setDouble(index,rs2.getDouble(j));
                break;
            case Types.NVARCHAR:
                ps4.setString(index,rs2.getString(j));
                break;
            case Types.OTHER:
                ps4.setObject(index,rs2.getObject(j));
                break;
            case Types.REAL:
                ps4.setDouble(index,rs2.getDouble(j));
                break;
            case Types.SMALLINT:
                ps4.setShort(index,rs2.getShort(j));
                break;
            case Types.TIME:
                ps4.setTime(index,rs2.getTime(j));
                break;
            case Types.TIMESTAMP:
                ps4.setTimestamp(index,rs2.getTimestamp(j));
                break;
            case Types.VARBINARY:
                ps4.setBytes(index,rs2.getBytes(j));
                break;
            case Types.VARCHAR:
                ps4.setString(index,rs2.getString(j));
                break;

        }
    }
}
