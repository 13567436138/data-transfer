package com.youben.task;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-17
 * Time: 17:01
 */
public class RestartTaskThread extends Thread {
    private int taskId;
    public RestartTaskThread(int taskId){
        this.taskId=taskId;
    }
    @Override
    public void run() {

    }
}
