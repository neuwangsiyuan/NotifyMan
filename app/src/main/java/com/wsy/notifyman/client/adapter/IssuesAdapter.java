package com.wsy.notifyman.client.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wsy.notifyman.R;
import com.wsy.notifyman.model.LocalIssue;

import dong.lan.base.BaseItemClickListener;
import dong.lan.library.LabelTextView;
import io.realm.RealmResults;

/**
 * Created by 思远 on 2017/5/7.
 */

public class IssuesAdapter extends RecyclerView.Adapter<IssuesAdapter.ViewHolder> {


    private RealmResults<LocalIssue> issues;

    public IssuesAdapter(RealmResults<LocalIssue> issues) {
        this.issues = issues;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_issue,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LocalIssue apply = issues.get(position);
        apply.show(holder);
    }

    @Override
    public int getItemCount() {
        return issues == null ? 0: issues.size();
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

            if(clickListener!=null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        issues.get(getLayoutPosition()).jump(itemView.getContext());
                    }
                });
            }
        }
    }

   private BaseItemClickListener<LocalIssue> clickListener;

    public void setClickListener(BaseItemClickListener<LocalIssue> clickListener) {
        this.clickListener = clickListener;
    }
}
