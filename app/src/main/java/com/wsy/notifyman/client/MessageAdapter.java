package com.wsy.notifyman.client;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.wsy.notifyman.Config;
import com.wsy.notifyman.R;
import com.wsy.notifyman.model.LocalMessage;
import com.wsy.notifyman.model.MyMessage;

import io.realm.RealmResults;

/**
 * Created by 思远 on 2017/5/7.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {


    private RealmResults<LocalMessage> applies;

    public MessageAdapter(RealmResults<LocalMessage> applies) {
        this.applies = applies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_message,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LocalMessage localMessage = applies.get(position);
        MyMessage myMessage = JSON.parseObject(localMessage.data,MyMessage.class);

//        if(apply.status == Config.APPLY_NEW){
            holder.handler.setText("已阅");
//            holder.handler.setEnabled(true);
//        }else{
//            holder.handler.setText("已同意");
//            holder.handler.setEnabled(false);
//        }
        holder.reason.setText(myMessage.data);
        //holder.username.setText(apply.applyUser);
    }

    @Override
    public int getItemCount() {
        return applies == null ? 0:applies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView username;
        TextView reason;
        Button handler;

        public ViewHolder(View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.item_apply_user);
            reason = (TextView) itemView.findViewById(R.id.item_apply_reason);
            handler = (Button) itemView.findViewById(R.id.item_apply_handler);

            handler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    applyAgree(getLayoutPosition());
                }
            });
        }
    }

    private void applyAgree(int position) {
        LocalMessage apply = applies.get(position);
        if(applyCallBack!=null)
            applyCallBack.apply(apply);
    }

    public void setLocalMessageCallBack(LocalMessageCallBack callBack){
        applyCallBack = callBack;
    }

    private LocalMessageCallBack applyCallBack;


    public interface LocalMessageCallBack{
        void apply(LocalMessage apply);
    }
}
