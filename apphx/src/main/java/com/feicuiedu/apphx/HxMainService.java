package com.feicuiedu.apphx;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.feicuiedu.apphx.model.event.HxDisconnectEvent;
import com.feicuiedu.apphx.model.event.HxNewMsgEvent;
import com.feicuiedu.apphx.presentation.call.CallReceiver;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.controller.EaseUI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import timber.log.Timber;

/**
 * 此服务用来处理通知，和账号异地登录等异常情况
 */
public class HxMainService extends Service{

    private CallReceiver callReceiver;

    @Nullable @Override public IBinder onBind(Intent intent) {
        return null;
    }

    @Override public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        registerCallReceiver();
    }


    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(callReceiver);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxDisconnectEvent event) {

        if (event.errorCode == EMError.USER_LOGIN_ANOTHER_DEVICE) {
            Toast.makeText(this, R.string.hx_error_account_conflict, Toast.LENGTH_SHORT).show();
        } else if (event.errorCode == EMError.USER_REMOVED) {
            Toast.makeText(this, R.string.hx_error_account_removed, Toast.LENGTH_SHORT).show();
        }
        exit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxNewMsgEvent event){

        Timber.d("HxNewMsgEvent");

        for (EMMessage message : event.list) {
            EaseUI.getInstance().getNotifier().onNewMsg(message);
        }
    }

    private void exit(){
        PackageManager pm = getPackageManager();

        // 获取当前应用的启动Intent
        Intent intent = pm.getLaunchIntentForPackage(getPackageName());
        // 清除所有的旧Activity
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("AUTO_LOGIN", true);
        startActivity(intent);
    }

    private void registerCallReceiver() {
        callReceiver = new CallReceiver();
        String callAction = EMClient.getInstance().callManager().getIncomingCallBroadcastAction();
        IntentFilter filter = new IntentFilter(callAction);
        registerReceiver(callReceiver, filter);
    }
}
