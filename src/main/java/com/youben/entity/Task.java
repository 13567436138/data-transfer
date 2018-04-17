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
    private int recordCount;
    private int successCount;
    private int failCount;
    private Date startTime;
    private Date stopTime;
    private Date recordModifyTimeBegin;
    private Date recordModifyTimeEnd;
    private int status;

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
