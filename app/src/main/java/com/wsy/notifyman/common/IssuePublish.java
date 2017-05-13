package com.wsy.notifyman.common;

import com.wsy.notifyman.model.master.Issue;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by 思远 on 2017/5/12.
 * describe ：
 */

public class IssuePublish extends Observable{


    private IssuePublish() {
        //no instance

    }

    private static IssuePublish publish = new IssuePublish();

    public static IssuePublish get(){
        return publish;
    }


    public void add(Observer observer){
        addObserver(observer);
    }

    public void publish(Issue issue){
        notifyObservers(issue);
    }

}
