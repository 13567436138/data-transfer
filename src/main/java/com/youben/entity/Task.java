package com.youben.entity;

import com.youben.base.GenericEntity;

import java.util.Date;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-17
 * Time: 10:47
 */
public class Task extends GenericEntity {
    private int mainTaskId;
    private String name;
    private int recordCount;
    private int successCount;
    private int failCount;
    private Date startTime;
    private Date stopTime;
    private Date recordModifyTimeBegin;
    private Date recordModifyTimeEnd;
    private int status; //1新建，2启动运行中，3成功，4重新启动运行中，5重新启动成功，6失败，7重新启动失败
    private int runCount;//运行次数

    private String mainTaskName;
    private String startTimeStr;
    private String stopTimeStr;
    private String recordModifyTimeBeginStr;
    private String recordModifyTimeEndStr;

    private boolean continueLast;

    public boolean isContinueLast() {
        return continueLast;
    }

    public void setContinueLast(boolean continueLast) {
        this.continueLast = continueLast;
    };

    public String getMainTaskName() {
        return mainTaskName;
    }

    public void setMainTaskName(String mainTaskName) {
        this.mainTaskName = mainTaskName;
    }

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

    public String getRecordModifyTimeBeginStr() {
        return recordModifyTimeBeginStr;
    }

    public void setRecordModifyTimeBeginStr(String recordModifyTimeBeginStr) {
        this.recordModifyTimeBeginStr = recordModifyTimeBeginStr;
    }

    public String getRecordModifyTimeEndStr() {
        return recordModifyTimeEndStr;
    }

    public void setRecordModifyTimeEndStr(String recordModifyTimeEndStr) {
        this.recordModifyTimeEndStr = recordModifyTimeEndStr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRunCount() {
        return runCount;
    }

    public void setRunCount(int runCount) {
        this.runCount = runCount;
    }

    public int getMainTaskId() {
        return mainTaskId;
    }

    public void setMainTaskId(int mainTaskId) {
        this.mainTaskId = mainTaskId;
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

    public Date getRecordModifyTimeBegin() {
        return recordModifyTimeBegin;
    }

    public void setRecordModifyTimeBegin(Date recordModifyTimeBegin) {
        this.recordModifyTimeBegin = recordModifyTimeBegin;
    }

    public Date getRecordModifyTimeEnd() {
        return recordModifyTimeEnd;
    }

    public void setRecordModifyTimeEnd(Date recordModifyTimeEnd) {
        this.recordModifyTimeEnd = recordModifyTimeEnd;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
