package com.wsy.notifyman.model.master;

import com.wsy.notifyman.server.PC;

/**
 * Created by 思远 on 2017/5/12.
 * 负责生产具体的故障消息
 */

public class IssueFactory {


    public static Issue build(PC pc) {
        int level = 0;
        StringBuilder sb = new StringBuilder();
        if (pc.getCpuRate() >= 80) {
            sb.append("CPU负载过高（");
            sb.append(pc.getCpuRate());
            sb.append("%)\n");
            level++;
        }
        float rate = pc.getMemoryUse() / pc.getMemoryCount();
        if (rate >= 0.8) {
            sb.append("内存负载过高（");
            sb.append(((rate * 100)));
            sb.append("%)\n");
            level++;
        }
        rate = pc.getStorageUse() / pc.getStorageCount();
        if (rate >= 0.8) {
            sb.append("磁盘负载过高（");
            sb.append(((rate * 100)));
            sb.append("%)\n");
            level++;
        }
        if (pc.getTemperature() >= 45) {
            sb.append("机房温度过高（");
            sb.append(pc.getTemperature());
            sb.append("℃)\n");
            level++;
        }
        if (pc.getHumidity() >= 80) {
            sb.append("机房湿度指数过高（");
            sb.append(pc.getHumidity());
            sb.append(")\n");
            level++;
        }

        if (pc.getNetRate() >= 80) {
            sb.append("服务器网络负载过高（");
            sb.append(pc.getNetRate());
            sb.append("%)\n");
            level++;
        }
        Issue issue = new Issue();
        issue.desc = sb.toString();
        issue.level = level;
        issue.setPc(PC.get());

        return issue;
    }
}
