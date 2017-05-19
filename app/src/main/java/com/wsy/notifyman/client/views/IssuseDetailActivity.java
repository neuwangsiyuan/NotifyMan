package com.wsy.notifyman.client.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.wsy.notifyman.R;
import com.wsy.notifyman.model.master.Issue;

import dong.lan.base.ui.BaseActivity;
import dong.lan.base.utils.DateUtils;

/**
 * Created by 思远 on 2017/5/15.
 * describe ：
 */

public class IssuseDetailActivity extends BaseActivity {

    private TextView issueInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_detail);
        issueInfo = (TextView) findViewById(R.id.issue_info);


        String issueStr = getIntent().getStringExtra("issue");
        if(TextUtils.isEmpty(issueStr)){
            finish();
        }else{
            Issue issue = JSON.parseObject(issueStr,Issue.class);
            issueInfo.append("故障等级：");
            issueInfo.append(issue.getLevel()+"\n");
            issueInfo.append(issue.getDesc());
            issueInfo.append("\n故障时间：");
            issueInfo.append(DateUtils.getTime(issue.getTimestamp(),"yyyy-MM-dd HH:mm"));
        }
    }
}
