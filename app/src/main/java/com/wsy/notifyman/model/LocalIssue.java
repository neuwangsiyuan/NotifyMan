package com.wsy.notifyman.model;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.wsy.notifyman.client.IssuesAdapter;
import com.wsy.notifyman.model.master.Issue;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 思远 on 2017/5/15.
 * describe ：
 */

public class LocalIssue extends RealmObject {
    @PrimaryKey
    public String id;

    public String origin;

    public long createdAt;

    @Ignore
    Issue issue;

    public LocalIssue() {

    }

    public LocalIssue(Issue issue) {
        id = issue.getId();
        origin = JSON.toJSONString(issue);
        createdAt = System.currentTimeMillis();
    }

    public Issue toIssue() {
        return JSON.parseObject(origin, Issue.class);
    }

    public void show(IssuesAdapter.ViewHolder holder) {
    }

    public void jump(Context context) {

    }
}
