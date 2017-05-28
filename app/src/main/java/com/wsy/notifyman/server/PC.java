package com.wsy.notifyman.server;

import com.wsy.notifyman.event.UpdateStatusEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by 思远 on 2017/5/19.
 * describe ：
 */

public class PC {



    private int cpuRate = 0;
    private float memoryCount = 10000;
    private float memoryUse = 0;
    private float storageCount = 1000000;
    private float storageUse = 0;
    private float temperature = 0;
    private float humidity = 0;
    private float netRate = 0;


    public PC(PC pc) {
        this.cpuRate = pc.getCpuRate();
        this.memoryUse = pc.getMemoryUse();
        this.storageUse = pc.getStorageUse();
        this.temperature =pc. getTemperature();
        this.humidity = pc.getHumidity();
        this.netRate = pc.getNetRate();
    }

    public void setCpuRate(int cpuRate) {
        this.cpuRate = cpuRate;
    }

    public void setMemoryCount(float memoryCount) {
        this.memoryCount = memoryCount;
    }

    public void setMemoryUse(float memoryUse) {
        this.memoryUse = memoryUse;
    }

    public void setStorageCount(float storageCount) {
        this.storageCount = storageCount;
    }

    public void setStorageUse(float storageUse) {
        this.storageUse = storageUse;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public void setNetRate(float netRate) {
        this.netRate = netRate;
    }

    public static void setPc(PC pc) {
        PC.pc = pc;
    }

    public PC() {
    }

    public int getCpuRate() {
        return cpuRate;
    }

    public float getMemoryCount() {
        return memoryCount;
    }

    public float getMemoryUse() {
        return memoryUse;
    }

    public float getStorageCount() {
        return storageCount;
    }

    public float getStorageUse() {
        return storageUse;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getNetRate() {
        return netRate;
    }

    private static PC pc;

    public static PC get() {
        if (pc == null)
            pc = new PC();
        return pc;
    }

    public void init() {
        cpuRate = (int) (Math.random() * 50 + 50);
        memoryUse = (float) (Math.random() * (memoryCount / 2) + memoryCount / 2);
        storageUse = (float) (Math.random() * (storageCount / 2) + storageCount / 2);
        temperature = (float) (Math.random() * 30 + 20);
        humidity = (float) (Math.random() * 40 + 60);
        netRate = (float) (Math.random() * 60 + 40);
        EventBus.getDefault().post(new UpdateStatusEvent());

    }

    public boolean isAlert() {
        if (cpuRate >= 80)
            return true;
        if (memoryUse / memoryCount >= 0.8)
            return true;
        if (storageUse / storageCount >= 0.8)
            return true;
        if (temperature >= 45)
            return true;
        if (humidity >= 80)
            return true;
        if (netRate >= 80)
            return true;
        return false;
    }

    public String CpuInfo() {
        if (cpuRate == 0)
            return "初始化中..";
        return "CPU使用率 " + cpuRate + " %";
    }

    public String MemoryIfo() {
        if (memoryUse == 0)
            return "初始化中..";
        return "内存 " + memoryUse + "MB / " + memoryCount + "MB";
    }

    public String StorageInfo() {
        if (storageUse == 0)
            return "初始化中..";
        return "存储 " + storageUse + "GB / " + storageCount + "GB";
    }

    public String TempInfo() {
        return "温度 " + temperature + " ℃";
    }

    public String HumidityInfo() {
        return "湿度 " + humidity + " ";
    }

    public String NetInfo() {
        return "带宽占用 " + netRate + " %";
    }

    @Override
    public String toString() {
        return "PC{" +
                "cpuRate=" + cpuRate +
                ", memoryCount=" + memoryCount +
                ", memoryUse=" + memoryUse +
                ", storageCount=" + storageCount +
                ", storageUse=" + storageUse +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", netRate=" + netRate +
                '}';
    }

    public PC newPc() {
        try {
            return (PC) this.clone();
        } catch (CloneNotSupportedException e) {
            return this;
        }
    }


}
