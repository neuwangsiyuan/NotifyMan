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
        return (float) values.get("humidity");
    }

    public void setHumidity(float humidity) {
        values.put("humidity",humidity);
    }

    @Override
    public void build() {

    }
}
