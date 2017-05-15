package com.wsy.notifyman.server.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.wsy.notifyman.R;
import com.wsy.notifyman.common.Group;
import com.wsy.notifyman.server.UsersAdapter;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupMembersCallback;
import cn.jpush.im.android.api.model.UserInfo;
import dong.lan.base.BaseItemClickListener;
import dong.lan.base.ui.BaseFragment;

/**
 */

public class GroupFragment extends BaseFragment implements BaseItemClickListener<UserInfo> {

    public static GroupFragment newInstance(String tittle) {
        GroupFragment fragment = new GroupFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tittle", tittle);
        fragment.setArguments(bundle);
        return fragment;
    }

    private LRecyclerView groupList;
    private UsersAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (content == null) {
            content = inflater.inflate(R.layout.fragment_group, container, false);
            groupList = (LRecyclerView) content.findViewById(R.id.group_list);
            groupList.setLayoutManager(new GridLayoutManager(getContext(), 1));
            start(null);
        }

        return content;
    }


    @Override
    public void start(Object data) {
        if (isStart && isAdded()) {
            JMessageClient.getGroupMembers(Group.get().myGroupId(), new GetGroupMembersCallback() {
                @Override
                public void gotResult(int i, String s, List<UserInfo> list) {
                    if (i == 0) {
                        adapter = new UsersAdapter(list);
                        adapter.setClickListener(GroupFragment.this);
                        groupList.setAdapter(adapter);
                    } else {
                        dialog("获取用户组信息失败，错误码：" + i);
                    }
                }
            });
        }
        super.start(data);
    }

    @Override
    public void onClick(UserInfo data, int action, int position) {

    }
}
