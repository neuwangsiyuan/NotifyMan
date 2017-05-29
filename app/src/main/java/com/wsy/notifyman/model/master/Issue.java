package com.wsy.notifyman.model.master;

import com.wsy.notifyman.server.PC;

/**
 * Created by 思远 on 2017/5/12.
 */

public class Issue {


    private String seq;
    private int count;
    protected String id;
    protected String desc;
    protected long timestamp;
    protected int level;
    private PC pc;


    public Issue() {
        id = String.valueOf(System.currentTimeMillis());
        timestamp = System.currentTimeMillis();
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setPc(PC pc) {
        this.pc = pc;
    }

    public PC getPc() {
        return pc;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void countAdd() {
        count++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Issue issue = (Issue) o;

        return seq != null ? seq.equals(issue.seq) : issue.seq == null;
    }

    @Override
    public int hashCode() {
        return seq != null ? seq.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id='" + id + '\'' +
                ", desc='" + desc + '\'' +
                ", timestamp=" + timestamp +
                ", level=" + level +
                ", pc=" + pc +
                '}';
    }

    public void updatePCInfo(Issue issue) {
        PC copyPc = issue.getPc();
        char c[] = seq.toCharArray();
        for (int i = 0; i < c.length; i++) {
            switch (c[i]) {
                case '1':
                    pc.setCpuRate(copyPc.getCpuRate());
                    break;
                case '2':
                    pc.setMemoryUse(copyPc.getMemoryUse());
                    break;
                case '3':
                    pc.setStorageUse(copyPc.getStorageUse());
                    break;
                case '4':
                    pc.setTemperature(copyPc.getTemperature());
                    break;
                case '5':
                    pc.setHumidity(copyPc.getHumidity());
                    break;
                case '6':
                    pc.setNetRate(copyPc.getNetRate());
                    break;
            }
        }
        pc = null;
        pc = copyPc;
    }
}
