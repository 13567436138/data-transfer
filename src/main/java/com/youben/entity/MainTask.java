package com.youben.entity;

import com.youben.base.GenericEntity;

/**
 * Description:
 * User: hxp
 * Date: 2018-04-16
 * Time: 18:47
 */
public class MainTask extends GenericEntity{
    private String name;
    private int fromSource;
    private int toSource;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFromSource() {
        return fromSource;
    }

    public void setFromSource(int fromSource) {
        this.fromSource = fromSource;
    }

    public int getToSource() {
        return toSource;
    }

    public void setToSource(int toSource) {
        this.toSource = toSource;
    }
}
