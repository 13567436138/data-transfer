package com.youben.entity;

import com.youben.base.GenericEntity;

import java.util.Date;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-17
 * Time: 11:24
 */
public class TaskThread extends GenericEntity {
    private int taskId;
    private String name;
    private String tableName;
    private String startRecordId;
    private String stopRecordId;
    private int recordCount;
    private int successCount;
    private int failCount;
    private Date startTime;
    private Date stopTime;
    private int status;
    private int runCount;

    private String mainTaskName;
    private String taskName;
    private int mainTaskId;
    private String startTimeStr;
    private String stopTimeStr;

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getStopTimeStr() {
        return stopTimeStr;
    }

    public void setStopTimeStr(String stopTimeStr) {
        this.stopTimeStr = stopTimeStr;
    }

    public int getMainTaskId() {
        return mainTaskId;
    }

    public void setMainTaskId(int mainTaskId) {
        this.mainTaskId = mainTaskId;
    }

    public String getMainTaskName() {
        return mainTaskName;
    }

    public void setMainTaskName(String mainTaskName) {
        this.mainTaskName = mainTaskName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getStartRecordId() {
        return startRecordId;
    }

    public void setStartRecordId(String startRecordId) {
        this.startRecordId = startRecordId;
    }

    public String getStopRecordId() {
        return stopRecordId;
    }

    public void setStopRecordId(String stopRecordId) {
        this.stopRecordId = stopRecordId;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRunCount() {
        return runCount;
    }

    public void setRunCount(int runCount) {
        this.runCount = runCount;
    }
}
