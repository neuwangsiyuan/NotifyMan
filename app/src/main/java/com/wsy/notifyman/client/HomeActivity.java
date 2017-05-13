package com.wsy.notifyman.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.wsy.notifyman.common.SPHelper;
import com.wsy.notifyman.model.LocalMessage;
import com.wsy.notifyman.model.MyMessage;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupIDListCallback;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by 思远 on 2017/5/6.
 */

public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getCanonicalName();
    private EditText clientSentEt;
    private Button clientSendBtn;
    private long groupId;
    private Toolbar toolbar;
    private RecyclerView messageList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        initView();

        init();
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.client_home_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_search){
            toSearchUser();
        }else if(id== R.id.action_logout){
            JMessageClient.logout();
            SPHelper.get().putLong("groupId",0);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void toSearchUser() {
        //// TODO: 2017/5/7
        startActivity(new Intent(this,SearchServerActivity.class));
    }

    private void initView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        messageList = (RecyclerView) findViewById(R.id.messageList);
        messageList.setLayoutManager(new GridLayoutManager(this,1));
        clientSentEt = (EditText) findViewById(R.id.client_send_et);
        clientSendBtn = (Button) findViewById(R.id.client_send_btn);
        clientSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendText();
            }
        });
    }

    private void sendText() {
        String text = clientSentEt.getText().toString();
        if (TextUtils.isEmpty(text)) {
            toast("内容不能为空");
            return;
        }
        if(!isConv){
            toast("无法同步服务器id,请退出重试");
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
        RealmResults<LocalMessage> messages = Realm.getDefaultInstance().where(LocalMessage.class)
                .findAllAsync();

        messages.addChangeListener(new RealmChangeListener<RealmResults<LocalMessage>>() {
            @Override
            public void onChange(RealmResults<LocalMessage> element) {
                messageList.getAdapter().notifyDataSetChanged();
            }
        });
        messageList.setAdapter(new MessageAdapter(messages));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isConv)
            JMessageClient.exitConversation();
    }
}
