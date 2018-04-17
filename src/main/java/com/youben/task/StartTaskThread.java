package com.youben.task;

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
    public StartTaskThread(int taskId){
        this.taskId=taskId;
        executorService= Executors.newFixedThreadPool(100);
    }
    @Override
    public void run() {

    }
}
