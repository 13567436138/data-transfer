package com.youben.task;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.youben.entity.TaskThread;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-17
 * Time: 20:07
 */
public class DoTransferThread extends Thread {
    private int type;//1新的，2重做
    private DruidPooledConnection from;
    private DruidPooledConnection to;
    private TaskThread taskThread;

    public DoTransferThread(int type,DruidPooledConnection from,DruidPooledConnection to,TaskThread taskThread){
        this.type=type;
        this.from=from;
        this.to=to;
        this.taskThread=taskThread;
    }
    @Override
    public void run() {

    }
}
