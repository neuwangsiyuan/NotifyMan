package com.wsy.notifyman.model.master;

/**
 * Created by 思远 on 2017/5/12.
 */

public class TemperatureIssue extends Issue {


    public TemperatureIssue(String tips) {
        super();
        setDesc(tips);
    }

    public float getTemp() {
        return (float) values.get("temp");
    }

    public void setTemp(float temp) {
        values.put("temp",temp);
    }

    @Override
    public void build() {
    }
}
