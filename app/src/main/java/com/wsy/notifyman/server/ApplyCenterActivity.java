package com.wsy.notifyman.server;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.wsy.notifyman.Config;
import com.wsy.notifyman.R;
import com.wsy.notifyman.common.BaseActivity;
import com.wsy.notifyman.common.SPHelper;
import com.wsy.notifyman.model.Apply;

import java.util.Collections;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by 思远 on 2017/5/7.
 */

public class ApplyCenterActivity extends BaseActivity implements ApplyAdapter.ApplyCallBack {


    private RecyclerView applyList;
    private ApplyAdapter applyAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_center);
        applyList = (RecyclerView) findViewById(R.id.apply_list);
        applyList.setLayoutManager(new GridLayoutManager(this,1));

        init();
    }

    private void init() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Apply> applies = realm.where(Apply.class).findAllSortedAsync("applyTime", Sort.DESCENDING);
        applies.addChangeListener(new RealmChangeListener<RealmResults<Apply>>() {
            @Override
            public void onChange(RealmResults<Apply> element) {
                applyAdapter.notifyDataSetChanged();
            }
        });
        applyAdapter = new ApplyAdapter(applies);
        applyAdapter.setApplyCallBack(this);
        applyList.setAdapter(applyAdapter);


    }

    @Override
    public void apply(final Apply apply) {
        final String user = apply.applyUser;
        new AlertDialog.Builder(this)
                .setTitle("绑定申请")
                .setMessage(apply.applyReason)
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContactManager.acceptInvitation(apply.applyUser, "", new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if(i == 0){
                                    addToGroup(apply.applyUser);
                                    Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            RealmResults<Apply> applies = realm.where(Apply.class).endsWith("applyUser",user).findAll();
                                            for(Apply a : applies){
                                                a.status = Config.APPLY_AGREE;
                                            }
                                        }
                                    });
                                }else{
                                    toast("同意请求失败，"+s);
                                }
                            }
                        });
                    }
                }).show();
    }

    private void addToGroup(String user) {
        long groupId = SPHelper.get().getLong("groupId");
        if(groupId!=0 && !TextUtils.isEmpty(user)){
            JMessageClient.addGroupMembers(groupId, Collections.singletonList(user), new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    if(i == 0){
                        toast("入群成功");
                    }else{
                        toast("入群失败："+s);
                    }
                }
            });
        }
    }
}
