package com.wsy.notifyman.server;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wsy.notifyman.Config;
import com.wsy.notifyman.R;
import com.wsy.notifyman.common.BaseActivity;
import com.wsy.notifyman.common.Group;
import com.wsy.notifyman.common.IMEIUtils;
import com.wsy.notifyman.common.SPHelper;
import com.wsy.notifyman.model.MyMessage;
import com.wsy.notifyman.server.ui.AffairsFragment;
import com.wsy.notifyman.server.ui.GroupFragment;
import com.wsy.notifyman.server.ui.IssuesFragment;
import com.wsy.notifyman.server.ui.StatusFragment;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;
import cn.jpush.im.android.api.callback.GetGroupMembersCallback;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import dong.lan.base.ui.BaseFragment;

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
    private TextView serverName;
    private TextView serverGroupId;


    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private Fragment[] tabs;

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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_apply) {
            toApplyCenter();
        }
        return super.onOptionsItemSelected(item);
    }

    private void toApplyCenter() {
        startActivity(new Intent(this, ApplyCenterActivity.class));
    }


    private void initView() {

        tabs = new Fragment[4];
        tabs[0] = StatusFragment.newInstance("状态");
        tabs[1] = IssuesFragment.newInstance("故障");
        tabs[2] = GroupFragment.newInstance("成员");
        tabs[3] = AffairsFragment.newInstance("事务");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        serverGroupId = (TextView) findViewById(R.id.server_group_id);
        serverName = (TextView) findViewById(R.id.server_name);



        serverNameInput = (EditText) findViewById(R.id.server_name_et);
        serverInitBtn = (Button) findViewById(R.id.bind_server_btn);
        serverInitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createServerGroup();
            }
        });


//        serverSendEt = (EditText) findViewById(R.id.server_send_et);
//        serverSendBtn = (Button) findViewById(R.id.server_send_btn);
//        serverSendBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendText();
//            }
//        });
    }

    private void sendText() {
        String text = serverSendEt.getText().toString();
        if (TextUtils.isEmpty(text)) {
            toast("内容不能为空");
            return;
        }
        MyMessage myMessage = new MyMessage();
        myMessage.code = Config.MSG_TEXT;
        myMessage.data = text;
        Message message = JMessageClient.createGroupTextMessage(groupId, myMessage.toJson());
        message.setOnSendCompleteCallback(new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    toast("发送成功");
                } else {
                    toast("发送失败：" + i);
                }
            }
        });
        JMessageClient.sendMessage(message);
    }

    //创建以服务器帮绑定的群组
    private void createServerGroup() {
        String name = serverNameInput.getText().toString();
        if (TextUtils.isEmpty(name)) {
            toast("组名不能为空");
            return;
        }
        JMessageClient.createGroup(name, "", new CreateGroupCallback() {
            @Override
            public void gotResult(int i, String s, long l) {
                if (i == 0) {
                    toast("创建成功");
                    groupId = l;
                    serverGroupId.setText(String.valueOf(l));
                    serverInitBtn.setVisibility(View.GONE);
                    serverNameInput.setVisibility(View.GONE);
                    SPHelper.get().putLong("groupId", l);
                } else {
                    toast("创建失败：" + i + "->" + s);
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
                    Log.d(TAG, "gotResult: " + i + "->" + s);
                    if (i == 0) {
                        //登陆成功
                        serverName.setText(imei);
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
            serverName.setText(userInfo.getUserName());
            running();
        }
    }

    private void signUp(String imei) {
        JMessageClient.register(imei, imei, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    toast("创建服务实例成功");
                    init();
                } else {
                    toast("创建服务实例失败：" + i + "+" + s);
                }
            }
        });
    }

    //服务器逻辑开始
    private void running() {
        groupId = SPHelper.get().getLong("groupId");
        serverGroupId.setText(String.valueOf(groupId));
        if (groupId == 0) {
            serverInitBtn.setVisibility(View.VISIBLE);
            serverNameInput.setVisibility(View.VISIBLE);
        } else {
            JMessageClient.enterGroupConversation(groupId);
            JMessageClient.getGroupMembers(groupId, new GetGroupMembersCallback() {
                @Override
                public void gotResult(int i, String s, List<UserInfo> list) {
                    Group.get().init(groupId, list);
                    for(int x = 0;x<tabs.length;x++){
                        ((BaseFragment)tabs[x]).start("");
                    }
                }
            });


        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        JMessageClient.exitConversation();
    }


    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getItem(position).getArguments().getString("tittle");
        }
    }
}
