package com.wsy.notifyman.model.master;

import com.wsy.notifyman.server.PC;

/**
 * Created by 思远 on 2017/5/12.
 */

public  class Issue {

    protected String id;
    protected String desc;
    protected long timestamp;
    protected int level;
    private PC pc;


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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setPc(PC pc) {
        this.pc = pc;
    }

    public PC getPc() {
        return pc;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id='" + id + '\'' +
                ", desc='" + desc + '\'' +
                ", timestamp=" + timestamp +
                ", level=" + level +
                ", pc=" + pc +
                '}';
    }
}
