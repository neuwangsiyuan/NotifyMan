package com.wsy.notifyman.server.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wsy.notifyman.R;
import com.wsy.notifyman.common.Group;

import cn.jpush.im.android.api.model.UserInfo;
import dong.lan.base.BaseItemClickListener;
import dong.lan.base.ui.customView.CircleImageView;

/**
 * Created by 思远 on 2017/5/7.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {



    public UsersAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserInfo userInfo = Group.get().getUsers().get(position);
        Glide.with(holder.itemView.getContext())
                .load(userInfo.getAvatar())
                .error(R.drawable.user)
                .into(holder.avatar);
        holder.username.setText(userInfo.getUserName());
        holder.info.setText(String.valueOf(userInfo.getStar()));
    }

    private BaseItemClickListener<UserInfo> clickListener;

    public void setClickListener(BaseItemClickListener<UserInfo> clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return Group.get().getUsers().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        TextView info;
        CircleImageView avatar;

        public ViewHolder(View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.item_username);
            info = (TextView) itemView.findViewById(R.id.item_user_info);
            avatar = (CircleImageView) itemView.findViewById(R.id.item_user_avatar);


            if (clickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int p = getLayoutPosition();
                        clickListener.onClick(Group.get().getUsers().get(p), 0, p);
                    }
                });
            }
        }
    }


}
