package com.wsy.notifyman.server.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wsy.notifyman.Config;
import com.wsy.notifyman.R;
import com.wsy.notifyman.model.Apply;

import dong.lan.library.LabelTextView;
import io.realm.RealmResults;

/**
 * Created by 思远 on 2017/5/7.
 */

public class ApplyAdapter extends RecyclerView.Adapter<ApplyAdapter.ViewHolder> {


    private RealmResults<Apply> applies;

    public ApplyAdapter(RealmResults<Apply> applies) {
        this.applies = applies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_apply,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Apply apply = applies.get(position);
        if(apply.status == Config.APPLY_NEW){
            holder.handler.setText("同意");
            holder.handler.setEnabled(true);
        }else{
            holder.handler.setText("已同意");
            holder.handler.setEnabled(false);
        }
        holder.reason.setText(apply.applyReason);
        holder.username.setText(apply.applyUser);
        Log.d("TAG", "onBindViewHolder: "+apply);
    }

    @Override
    public int getItemCount() {
        return applies == null ? 0:applies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView username;
        TextView reason;
        LabelTextView handler;

        public ViewHolder(View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.item_apply_user);
            reason = (TextView) itemView.findViewById(R.id.item_apply_reason);
            handler = (LabelTextView) itemView.findViewById(R.id.item_apply_handler);

            handler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    applyAgree(getLayoutPosition());
                }
            });
        }
    }

    private void applyAgree(int position) {
        Apply apply = applies.get(position);
        if(applyCallBack!=null)
            applyCallBack.apply(apply);
    }

    public void setApplyCallBack(ApplyCallBack callBack){
        applyCallBack = callBack;
    }

    private ApplyCallBack applyCallBack;


    public interface ApplyCallBack{
        void apply(Apply apply);
    }
}
