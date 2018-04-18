package com.youben.task;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.youben.constant.DataTransferConst;
import com.youben.entity.TaskThread;

import java.sql.SQLException;

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

    public DoTransferThread(int type,DruidDataSource from,DruidDataSource to,TaskThread taskThread){
        this.type=type;
        this.from=from;
        this.to=to;
        this.taskThread=taskThread;
    }
    @Override
    public void run() {
        DruidPooledConnection fromConn = null;
        DruidPooledConnection toConn=null;
        try {
            fromConn =from.getConnection();
            toConn=to.getConnection();
            if(type== DataTransferConst.DO_JOB_TYPE_NEW){

            }else if(type==DataTransferConst.DO_JOB_TYPE_REDO){

            }
        } catch (SQLException e) {
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
        }
    }
}
