package com.wsy.notifyman.server.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wsy.notifyman.R;
import com.wsy.notifyman.common.Group;
import com.wsy.notifyman.model.master.Issue;

import dong.lan.base.utils.DateUtils;
import dong.lan.library.LabelTextView;

/**
 * Created by 思远 on 2017/5/7.
 */

public class IssuesAdapter extends RecyclerView.Adapter<IssuesAdapter.ViewHolder> {




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_issue,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Issue issue = Group.get().getIssues().get(position);
        if(issue!=null){
            holder.level.setText("等级："+issue.getLevel()+" 频次："+issue.getCount());
            holder.desc.setText(issue.getDesc());
            holder.params.setText(DateUtils.getTime(issue.getTimestamp(),"yyyy-MM-dd HH:mm"));
        }
    }

    @Override
    public int getItemCount() {
        return Group.get().getIssues().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView desc;
        public TextView params;
        public LabelTextView level;

        public ViewHolder(final View itemView) {
            super(itemView);

            desc = (TextView) itemView.findViewById(R.id.item_issue_desc);
            params = (TextView) itemView.findViewById(R.id.item_issue_params);
            level = (LabelTextView) itemView.findViewById(R.id.item_issue_level);


        }
    }


}
