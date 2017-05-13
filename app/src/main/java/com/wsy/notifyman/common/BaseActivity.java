package com.wsy.notifyman.common;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

/**
 * Created by 思远 on 2017/5/6.
 */

public class BaseActivity extends AppCompatActivity {


    public void toast(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }

}
