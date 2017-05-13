package com.wsy.notifyman.model.master;

/**
 * Created by 思远 on 2017/5/12.
 */

public class HumidityIssue extends Issue {

    private float humidity;

    public HumidityIssue(String tips) {
        super();
        setDesc(tips);
    }


    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    @Override
    public void build() {

    }
}
