package com.wsy.notifyman.server.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsy.notifyman.R;
import com.wsy.notifyman.server.adapter.IssuesAdapter;

import dong.lan.base.ui.BaseFragment;

/**
 * Created by 梁桂栋 on 2017/5/13.
 * Email: 760625325@qq.com
 * Github: github.com/donlan
 */

public class IssuesFragment extends BaseFragment {

    private RecyclerView issueList;

    public static IssuesFragment newInstance(String tittle) {
        IssuesFragment fragment = new IssuesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tittle", tittle);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(content == null){
            content = inflater.inflate(R.layout.fragment_server_issue,container,false);
            issueList = (RecyclerView) content.findViewById(R.id.issues_list);
            issueList.setLayoutManager(new GridLayoutManager(getContext(),1));
            start(null);
        }
        return content;
    }


    @Override
    public void start(Object data) {
        if(isStart && isAdded()){
            issueList.setAdapter(new IssuesAdapter());
        }
        super.start(data);
    }
}
