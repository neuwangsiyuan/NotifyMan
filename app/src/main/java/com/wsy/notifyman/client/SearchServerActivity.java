package com.wsy.notifyman.client;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.wsy.notifyman.Config;
import com.wsy.notifyman.R;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import dong.lan.base.ui.BaseActivity;

/**
 * Created by 思远 on 2017/5/7.
 */

public class SearchServerActivity extends BaseActivity {

    private EditText searchIn;
    private EditText reasonIn;
    private LinearLayout reasonLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serch_server);

        initView();
    }

    private void initView() {
        reasonIn = (EditText) findViewById(R.id.search_reason_et);
        reasonLayout = (LinearLayout) findViewById(R.id.server_reason_layout);
        findViewById(R.id.search_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apply(searchIn.getText().toString(),reasonIn.getText().toString());
            }
        });
        searchIn = (EditText) findViewById(R.id.search_server_et);
        findViewById(R.id.search_action_ib).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reasonLayout.setVisibility(View.GONE);
                search(searchIn.getText().toString());
            }
        });
    }

    private void apply(String username, String reason) {
        ContactManager.sendInvitationRequest(username, Config.API_KEY, reason, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if(i == 0){
                    toast("发送申请成功");
                }else{
                    toast("发送失败："+s);
                }
            }
        });
    }

    private void search(String username) {
        JMessageClient.getUserInfo(username, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                if(i == 0){
                    reasonLayout.setVisibility(View.VISIBLE);
                }else{
                    toast("搜索失败："+s);
                }
            }
        });
    }
}
