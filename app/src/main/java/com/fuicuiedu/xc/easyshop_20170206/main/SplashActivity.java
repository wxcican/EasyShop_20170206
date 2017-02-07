package com.fuicuiedu.xc.easyshop_20170206.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fuicuiedu.xc.easyshop_20170206.R;
import com.fuicuiedu.xc.easyshop_20170206.commons.ActivityUtils;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private ActivityUtils activityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        activityUtils = new ActivityUtils(this);

        // TODO: 2017/2/7 0007 环信登录相关（账号冲突踢出）
        // TODO: 2017/2/7 0007 判断用户是否登录，自动登录

        //1.5秒跳转到主页
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //跳转到主页
                activityUtils.startActivity(MainActivity.class);
                finish();
            }
        },1500);
    }
}
