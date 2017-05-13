package com.wsy.notifyman.model.master;

/**
 * Created by 思远 on 2017/5/12.
 */

public class TemperatureIssue extends Issue {

    private float temp;

    public TemperatureIssue(String tips) {
        super();
        setDesc(tips);
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    @Override
    public void build() {

    }
}
