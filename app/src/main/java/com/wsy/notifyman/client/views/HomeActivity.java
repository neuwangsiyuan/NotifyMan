package com.wsy.notifyman.client.views;

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
import com.wsy.notifyman.event.ClientRefreshEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupIDListCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import dong.lan.base.ui.BaseActivity;
import dong.lan.base.ui.BaseFragment;

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

        EventBus.getDefault().register(this);

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
        if (id == R.id.action_logout) {
            JMessageClient.logout();
            SPHelper.get().putLong("groupId", 0);
            finish();
        }
        return super.onOptionsItemSelected(item);
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
        // TODO: 2017/5/23 groupID
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



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(ClientRefreshEvent event){
        if(!isConv)
            running();

    }


    private void init() {
        running();
    }


    private boolean isConv = false;

    //客户端逻辑开始
    private void running() {
        JMessageClient.getGroupIDList(new GetGroupIDListCallback() {
            @Override
            public void gotResult(int i, String s, List<Long> list) {
                if (i == 0 && list != null && !list.isEmpty()) {
                    groupId = list.get(0);
                    serverName.setText(String.valueOf(groupId));
                    SPHelper.get().putLong("groupId", groupId);
                    JMessageClient.enterGroupConversation(groupId);
                    isConv = true;
                    toast("与服务器连接成功");
                    // TODO: 2017/5/23 tabs[0]).start(null)含义
                    ((BaseFragment)tabs[0]).start(null);
                }
            }
        });
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
        EventBus.getDefault().unregister(this);
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
