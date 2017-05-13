package com.wsy.notifyman.model.master;

/**
 * Created by 思远 on 2017/5/12.
 */

public abstract class Issue {

    protected String id;
    protected String desc;
    protected long timestamp;
    protected int level;


    public abstract void build();

    public Issue() {
        id = String.valueOf(System.currentTimeMillis());
        timestamp = System.currentTimeMillis();
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
