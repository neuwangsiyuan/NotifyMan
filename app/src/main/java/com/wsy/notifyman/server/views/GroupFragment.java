package com.wsy.notifyman.server.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsy.notifyman.R;
import com.wsy.notifyman.server.adapter.UsersAdapter;

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

    private RecyclerView groupList;
    private UsersAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (content == null) {
            content = inflater.inflate(R.layout.fragment_group, container, false);
            groupList = (RecyclerView) content.findViewById(R.id.group_list);
            groupList.setLayoutManager(new GridLayoutManager(getContext(), 1));
            start(null);
        }

        return content;
    }


    @Override
    public void start(Object data) {
        if ( isAdded() && isStart) {
            adapter = new UsersAdapter();
            adapter.setClickListener(GroupFragment.this);
            groupList.setAdapter(adapter);
        }
        super.start(data);
    }

    @Override
    public void onClick(UserInfo data, int action, int position) {

    }
}
