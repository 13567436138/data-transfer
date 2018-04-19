package com.youben.entity;

import com.youben.base.GenericEntity;

import java.util.Date;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-19
 * Time: 13:54
 */
public class MainTaskInfo extends GenericEntity {
    private String tableName;
    private int fromRecordCount;
    private Date fromRecordEalyDate;
    private Date fromRecordLateDate;
    private int toRecordCount;
    private Date toRecordEalyDate;
    private Date toRecordLateDate;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getFromRecordCount() {
        return fromRecordCount;
    }

    public void setFromRecordCount(int fromRecordCount) {
        this.fromRecordCount = fromRecordCount;
    }

    public Date getFromRecordEalyDate() {
        return fromRecordEalyDate;
    }

    public void setFromRecordEalyDate(Date fromRecordEalyDate) {
        this.fromRecordEalyDate = fromRecordEalyDate;
    }

    public Date getFromRecordLateDate() {
        return fromRecordLateDate;
    }

    public void setFromRecordLateDate(Date fromRecordLateDate) {
        this.fromRecordLateDate = fromRecordLateDate;
    }

    public int getToRecordCount() {
        return toRecordCount;
    }

    public void setToRecordCount(int toRecordCount) {
        this.toRecordCount = toRecordCount;
    }

    public Date getToRecordEalyDate() {
        return toRecordEalyDate;
    }

    public void setToRecordEalyDate(Date toRecordEalyDate) {
        this.toRecordEalyDate = toRecordEalyDate;
    }

    public Date getToRecordLateDate() {
        return toRecordLateDate;
    }

    public void setToRecordLateDate(Date toRecordLateDate) {
        this.toRecordLateDate = toRecordLateDate;
    }
}
