package com.wsy.notifyman.model.master;

import com.wsy.notifyman.Config;

/**
 * Created by 思远 on 2017/5/12.
 * 负责生产具体的故障消息
 */

public class IssueFactory {


    public static final int TAG = 10000;
    public static final int COUNT = 6;
    public static final int BUCKET[] = new int[COUNT];

    /**
     * 打乱随机数组
     */
    public static void CHAOS() {
        for (int i = 0; i < COUNT; i++) {
            int x = (int) (Math.random() * TAG / (COUNT - i));
            BUCKET[i] = x;
        }
    }

    /**
     * 从随机数据里找第一个比当前随机数大的索引位置
     *
     * @param x
     * @return
     */
    public static int Pick(int x) {
        for (int i = 0; i < COUNT; i++) {
            if (BUCKET[i] > x)
                return i;
        }
        int t = (int) (Math.random() * TAG % (COUNT * COUNT));
        return t < COUNT ? t : -1;
    }

    public static Issue build(int type) {
        switch (type) {
            case 0:

                break;
            case 1:

                break;
            case 2:


                break;
            case 3:


                break;

            case 4:

                break;
            case 5:

                break;
            default:
                return null;
        }
        return null;
    }
}
