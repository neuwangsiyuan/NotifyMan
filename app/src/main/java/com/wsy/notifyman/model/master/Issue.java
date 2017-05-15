package com.wsy.notifyman.model.master;

import java.util.Map;

/**
 * Created by 思远 on 2017/5/12.
 */

public abstract class Issue {

    protected String id;
    protected String desc;
    protected long timestamp;
    protected int level;
    protected Map<String,Object> values;


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

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }
}
