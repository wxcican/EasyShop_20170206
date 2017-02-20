package com.feicuiedu.apphx;


import android.app.ActivityManager;
import android.app.Application;
import android.content.Intent;

import com.feicuiedu.apphx.easeui.HxNotificationInfoProvider;
import com.feicuiedu.apphx.easeui.HxUserProfileProvider;
import com.feicuiedu.apphx.model.repository.DefaultLocalInviteRepo;
import com.feicuiedu.apphx.model.repository.DefaultLocalUsersRepo;
import com.feicuiedu.apphx.model.repository.MockRemoteUsersRepo;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;

import java.util.List;

import timber.log.Timber;

/**
 * Application基类。使用此模块的app需要继承此类，并重写{@link #initHxModule(HxModuleInitializer)}方法。
 */
public class HxBaseApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();

        // 只在App进程中做初始化操作，防止环信SDK被初始化多次。
        if (isAppProcess()) {
            // 初始化Timber日志
            Timber.plant(new Timber.DebugTree());

            // 初始化EaseUI
            initEaseUI();

            // 初始化AppHx模块
            initHxModule(HxModuleInitializer.getInstance());

            // 启动后台服务，处理通知等
            Intent service = new Intent(this, HxMainService.class);
            startService(service);
        }

    }


    // Note: 子类需重写此方法
    protected void initHxModule(HxModuleInitializer initializer) {
        initializer.setRemoteUsersRepo(new MockRemoteUsersRepo())
                .setLocalUsersRepo(DefaultLocalUsersRepo.getInstance(this))
                .setLocalInviteRepo(DefaultLocalInviteRepo.getInstance(this))
                .init();
    }


    /**
     * 初始化环信SDK和EaseUI库
     */
    private void initEaseUI() {
        EMOptions options = new EMOptions();
        // 关闭自动登录
        options.setAutoLogin(false);

        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);

        EaseUI.getInstance().init(this, options);

        EaseUI.getInstance().getNotifier().setNotificationInfoProvider(new HxNotificationInfoProvider(this));

        EaseUI.getInstance().setUserProfileProvider(HxUserProfileProvider.getInstance());

        // 关闭环信日志
        EMClient.getInstance().setDebugMode(false);
    }

    /**
     * 判断Application是否在App进程中启动。
     */
    private boolean isAppProcess() {
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);

        // 如果APP启用了远程的service，此application:onCreate会被调用2次，例如使用百度定位服务时。
        // 默认的APP进程名为APP包名，如果查到的process name不是APP的process name就返回false。

        //noinspection RedundantIfStatement
        if (processAppName == null || !processAppName.equalsIgnoreCase(getPackageName())) {
            // 则此application::onCreate 是被service 调用的。
            return false;
        }
        return true;
    }

    // 获取进程名称
    private String getAppName(int pID) {
        String processName;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : processes) {

            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}
