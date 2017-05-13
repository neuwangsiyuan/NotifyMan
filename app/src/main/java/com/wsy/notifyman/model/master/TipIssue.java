package com.wsy.notifyman.model.master;

/**
 * Created by 思远 on 2017/5/12.
 */

public class TipIssue extends Issue {

    public TipIssue(String tips) {
        super();
        setDesc(tips);
    }


    @Override
    public void build() {

    }
}
