package com.fuicuiedu.xc.easyshop_20170206.network;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 方法内代码能在主线程中运行的CallBack
 */

public abstract class UICallBack implements Callback{

    //拿到主线程的handler
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onFailure(final Call call, final IOException e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onFailureUI(call, e);
            }
        });
    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onResponseUI(call, response);
            }
        });
    }

    //抽象两个方法（运行主线程）
    public abstract void onFailureUI(Call call, IOException e);
    public abstract void onResponseUI(Call call, Response response);

}
