package com.wsy.notifyman.model;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.wsy.notifyman.client.adapter.IssuesAdapter;
import com.wsy.notifyman.client.views.IssuseDetailActivity;
import com.wsy.notifyman.model.master.Issue;

import dong.lan.base.utils.DateUtils;
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
        if (issue == null)
            issue = JSON.parseObject(origin, Issue.class);
        if (issue != null) {
            holder.level.setText(String.valueOf(issue.getLevel()));
            holder.desc.setText(issue.getDesc());
            holder.params.setText(DateUtils.getTime(issue.getTimestamp(), "yyyy-MM-dd HH:mm"));
        }
    }

    @Override
    public String toString() {
        return "LocalIssue{" +
                "id='" + id + '\'' +
                ", origin='" + origin + '\'' +
                ", createdAt=" + createdAt +
                ", issue=" + issue +
                '}';
    }

    public void jump(Context context) {
        Intent intent = new Intent(context, IssuseDetailActivity.class);
        intent.putExtra("issue", origin);
        context.startActivity(intent);


    }
}
