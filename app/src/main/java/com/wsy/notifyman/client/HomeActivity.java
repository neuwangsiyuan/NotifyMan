package com.wsy.notifyman.client;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wsy.notifyman.Config;
import com.wsy.notifyman.R;
import com.wsy.notifyman.common.SPHelper;

import java.util.List;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupIDListCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import dong.lan.base.ui.BaseActivity;

/**
 * Created by 思远 on 2017/5/6.
 */

public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getCanonicalName();
    private Button serverInitBtn;
    private long groupId;
    private TextView serverName;
    private EditText bindServerReason;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private Fragment[] tabs;
    private EditText serverNameInput;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        initView();

        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.client_home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            toSearchUser();
        } else if (id == R.id.action_logout) {
            JMessageClient.logout();
            SPHelper.get().putLong("groupId", 0);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void toSearchUser() {
        //// TODO: 2017/5/7
        startActivity(new Intent(this, SearchServerActivity.class));
    }

    private void initView() {

        tabs = new Fragment[1];
        tabs[0] = ServerIssuesFragment.newInstance("故障信息");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        serverName = (TextView) findViewById(R.id.server_name);

        bindServerReason = (EditText) findViewById(R.id.bind_server_reason_et);

        serverNameInput = (EditText) findViewById(R.id.server_name_et);
        serverInitBtn = (Button) findViewById(R.id.bind_server_btn);
        serverInitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindServer();
            }
        });

        groupId = SPHelper.get().getLong("groupId");
        if (groupId == 0) {
            serverNameInput.setVisibility(View.VISIBLE);
            bindServerReason.setVisibility(View.VISIBLE);
            serverInitBtn.setVisibility(View.VISIBLE);
        }
    }

    private void bindServer() {
        String serverName = serverNameInput.getText().toString();
        if (TextUtils.isEmpty(serverName)) {
            dialog("请输入正确的服务名");
            return;
        }
        search(serverName);
    }

    private void sendText() {
//        String text = clientSentEt.getText().toString();
//        if (TextUtils.isEmpty(text)) {
//            toast("内容不能为空");
//            return;
//        }
//        if(!isConv){
//            toast("无法同步服务器id,请退出重试");
//            return;
//        }
//        MyMessage myMessage = new MyMessage();
//        myMessage.code = Config.MSG_TEXT;
//        myMessage.data = text;
//        Message message = JMessageClient.createGroupTextMessage(groupId, myMessage.toJson());
//        message.setOnSendCompleteCallback(new BasicCallback() {
//            @Override
//            public void gotResult(int i, String s) {
//                if (i == 0) {
//                    toast("发送成功");
//                } else {
//                    toast("发送失败：" + i);
//                }
//            }
//        });
//        JMessageClient.sendMessage(message);
    }


    private void init() {
        running();
    }


    private boolean isConv = false;

    //服务器逻辑开始
    private void running() {
        JMessageClient.getGroupIDList(new GetGroupIDListCallback() {
            @Override
            public void gotResult(int i, String s, List<Long> list) {
                if (i == 0 && list != null && !list.isEmpty()) {
                    groupId = list.get(0);
                    SPHelper.get().putLong("groupId", groupId);
                    JMessageClient.enterGroupConversation(groupId);
                    isConv = true;
                    toast("与服务器连接成功");
                }
            }
        });
//        RealmResults<LocalMessage> messages = Realm.getDefaultInstance().where(LocalMessage.class)
//                .findAllAsync();
//
//        messages.addChangeListener(new RealmChangeListener<RealmResults<LocalMessage>>() {
//            @Override
//            public void onChange(RealmResults<LocalMessage> element) {
//                messageList.getAdapter().notifyDataSetChanged();
//            }
//        });
//        messageList.setAdapter(new MessageAdapter(messages));
    }

    private void apply(String username, String reason) {
        ContactManager.sendInvitationRequest(username, Config.API_KEY, reason, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    toast("发送绑定成功");
                    serverNameInput.setVisibility(View.GONE);
                    bindServerReason.setVisibility(View.GONE);
                    serverInitBtn.setVisibility(View.GONE);
                } else {
                    toast("发送绑定失败：" + s);
                }
            }
        });
    }

    private void search(final String username) {
        JMessageClient.getUserInfo(username, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                if (i == 0) {
                    apply(username, bindServerReason.getText().toString());
                } else {
                    toast("获取服务器信息失败：" + s);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isConv)
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
