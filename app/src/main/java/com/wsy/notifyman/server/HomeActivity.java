package com.wsy.notifyman.server;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wsy.notifyman.Config;
import com.wsy.notifyman.R;
import com.wsy.notifyman.common.BaseActivity;
import com.wsy.notifyman.common.Group;
import com.wsy.notifyman.common.IMEIUtils;
import com.wsy.notifyman.common.SPHelper;
import com.wsy.notifyman.model.MyMessage;

import java.util.Collections;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;
import cn.jpush.im.android.api.callback.GetGroupMembersCallback;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by 思远 on 2017/5/6.
 */

public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getCanonicalName();
    private EditText serverNameInput;
    private Button serverInitBtn;

    private EditText serverSendEt;
    private Button serverSendBtn;
    private long groupId;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_home);
        startService(new Intent(this, CreatorServer.class));
        startService(new Intent(this, InterceptService.class));
        initView();

        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.server_home_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_apply){
            toApplyCenter();
        }
        return super.onOptionsItemSelected(item);
    }

    private void toApplyCenter() {
        startActivity(new Intent(this,ApplyCenterActivity.class));
    }


    private void initView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        serverNameInput = (EditText) findViewById(R.id.server_name_et);
        serverInitBtn = (Button) findViewById(R.id.server_init_btn);
        serverInitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createServerGroup();
            }
        });


        serverSendEt = (EditText) findViewById(R.id.server_send_et);
        serverSendBtn = (Button) findViewById(R.id.server_send_btn);
        serverSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendText();
            }
        });
    }

    private void sendText() {
        String text = serverSendEt.getText().toString();
        if(TextUtils.isEmpty(text)){
            toast("内容不能为空");
            return;
        }
        MyMessage myMessage = new MyMessage();
        myMessage.code = Config.MSG_TEXT;
        myMessage.data = text;
        Message message = JMessageClient.createGroupTextMessage(groupId,myMessage.toJson());
        message.setOnSendCompleteCallback(new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if(i == 0){
                    toast("发送成功");
                }else{
                    toast("发送失败："+i);
                }
            }
        });
        JMessageClient.sendMessage(message);
    }

    //创建以服务器帮绑定的群组
    private void createServerGroup() {
        String name = serverNameInput.getText().toString();
        if(TextUtils.isEmpty(name)){
            toast("组名不能为空");
            return;
        }
        JMessageClient.createGroup(name, "", new CreateGroupCallback() {
            @Override
            public void gotResult(int i, String s, long l) {
                if(i == 0){
                    toast("创建成功");
                    SPHelper.get().putLong("groupId",l);
                }else{
                    toast("创建失败："+i+"->"+s);
                }
            }
        });
    }

    private void init() {
        UserInfo userInfo = JMessageClient.getMyInfo();
        if (userInfo == null) {
            //未登录
            final String imei = IMEIUtils.getImei(this);
            JMessageClient.login(imei, imei, new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    Log.d(TAG, "gotResult: "+i+"->"+s);
                    if (i == 0) {
                        //登陆成功
                        running();
                    } else if (i == 801003) { //没注册过的
                        signUp(imei);
                    } else {
                        toast(s);
                    }
                }
            });
        } else {
            //登录了
            running();
        }
    }

    private void signUp(String imei) {
        JMessageClient.register(imei, imei, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if(i == 0){
                    JMessageClient.addGroupMembers(groupId, Collections.singletonList("test"), new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            toast(i+"->"+s);
                        }
                    });
                    toast("创建服务实例成功");
                    init();
                }else{
                    toast("创建服务实例失败："+i+"+"+s);
                }
            }
        });
    }

    //服务器逻辑开始
    private void running() {
        groupId =  SPHelper.get().getLong("groupId");
        if(groupId == 0){
            serverInitBtn.setVisibility(View.VISIBLE);
            serverNameInput.setVisibility(View.VISIBLE);
        }else{
            JMessageClient.enterGroupConversation(groupId);
            JMessageClient.getGroupMembers(groupId, new GetGroupMembersCallback() {
                @Override
                public void gotResult(int i, String s, List<UserInfo> list) {
                    Group.get().init(groupId,list);
                }
            });

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        JMessageClient.exitConversation();
    }
}
