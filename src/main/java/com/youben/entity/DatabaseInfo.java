package com.youben.entity;

import com.youben.base.GenericEntity;

import java.util.Date;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-19
 * Time: 13:53
 */
public class DatabaseInfo extends GenericEntity {
    private String tableName;
    private int recordCount;
    private Date recordEalyDate;
    private Date recordLateDate;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public Date getRecordEalyDate() {
        return recordEalyDate;
    }

    public void setRecordEalyDate(Date recordEalyDate) {
        this.recordEalyDate = recordEalyDate;
    }

    public Date getRecordLateDate() {
        return recordLateDate;
    }

    public void setRecordLateDate(Date recordLateDate) {
        this.recordLateDate = recordLateDate;
    }
}
