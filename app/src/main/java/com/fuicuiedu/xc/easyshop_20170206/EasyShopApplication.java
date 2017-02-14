package com.fuicuiedu.xc.easyshop_20170206;

import android.app.Application;

import com.fuicuiedu.xc.easyshop_20170206.model.CachePreferences;

/**
 * Created by Administrator on 2017/2/14 0014.
 */

public class EasyShopApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化本地配置
        CachePreferences.init(this);
    }
}
