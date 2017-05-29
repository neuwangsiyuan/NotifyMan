package com.wsy.notifyman.server.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wsy.notifyman.R;
import com.wsy.notifyman.event.UpdateStatusEvent;
import com.wsy.notifyman.server.PC;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import dong.lan.base.ui.BaseFragment;

/**
 */

public class StatusFragment extends BaseFragment {

    //创建碎片显示
    public static StatusFragment newInstance(String tittle) {
        StatusFragment fragment = new StatusFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tittle", tittle);
        fragment.setArguments(bundle);
        return fragment;
    }

    private TextView cpu;
    private TextView memory;
    private TextView storage;
    private TextView temp;
    private TextView humidity;
    private TextView net;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (content == null) {
            content = inflater.inflate(R.layout.fragment_status, container, false);
            cpu = (TextView) content.findViewById(R.id.status_cpu);
            memory = (TextView) content.findViewById(R.id.status_memory);
            storage = (TextView) content.findViewById(R.id.status_storage);
            temp = (TextView) content.findViewById(R.id.status_temperature);
            humidity = (TextView) content.findViewById(R.id.status_humidity);
            net = (TextView) content.findViewById(R.id.status_net);

            showServerStatus();

            EventBus.getDefault().register(this);
        }

        return content;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPcStatusChange(UpdateStatusEvent event){
        showServerStatus();
    }

    private void showServerStatus() {

        cpu.setText(PC.get().CpuInfo());
        memory.setText(PC.get().MemoryIfo());
        storage.setText(PC.get().StorageInfo());
        temp.setText(PC.get().TempInfo());
        humidity.setText(PC.get().HumidityInfo());
        net.setText(PC.get().NetInfo());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
