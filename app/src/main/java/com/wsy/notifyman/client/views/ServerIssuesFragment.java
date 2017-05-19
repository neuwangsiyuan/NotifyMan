package com.wsy.notifyman.client.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsy.notifyman.R;
import com.wsy.notifyman.client.adapter.IssuesAdapter;
import com.wsy.notifyman.model.LocalIssue;

import dong.lan.base.ui.BaseFragment;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 */

public class ServerIssuesFragment extends BaseFragment {

    private IssuesAdapter applyAdapter;

    public static ServerIssuesFragment newInstance(String tittle) {
        ServerIssuesFragment fragment = new ServerIssuesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tittle", tittle);
        fragment.setArguments(bundle);
        return fragment;
    }


    private RecyclerView issueList;

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
        if(isAdded() && isStart){
            Realm realm = Realm.getDefaultInstance();
            RealmResults<LocalIssue> applies = realm.where(LocalIssue.class).findAllSortedAsync("createdAt", Sort.DESCENDING);
            applies.addChangeListener(new RealmChangeListener<RealmResults<LocalIssue>>() {
                @Override
                public void onChange(RealmResults<LocalIssue> element) {
                    applyAdapter.notifyDataSetChanged();
                }
            });
            applyAdapter = new IssuesAdapter(applies);
            issueList.setAdapter(applyAdapter);
        }
        super.start(data);
    }
}
