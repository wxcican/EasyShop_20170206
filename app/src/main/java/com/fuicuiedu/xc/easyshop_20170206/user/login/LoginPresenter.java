package com.fuicuiedu.xc.easyshop_20170206.user.login;

import com.feicuiedu.apphx.model.HxUserManager;
import com.feicuiedu.apphx.model.event.HxErrorEvent;
import com.feicuiedu.apphx.model.event.HxEventType;
import com.feicuiedu.apphx.model.event.HxSimpleEvent;
import com.fuicuiedu.xc.easyshop_20170206.commons.CurrentUser;
import com.fuicuiedu.xc.easyshop_20170206.model.CachePreferences;
import com.fuicuiedu.xc.easyshop_20170206.model.User;
import com.fuicuiedu.xc.easyshop_20170206.model.UserResult;
import com.fuicuiedu.xc.easyshop_20170206.network.EasyShopClient;
import com.fuicuiedu.xc.easyshop_20170206.network.UICallBack;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.hyphenate.easeui.domain.EaseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/2/14 0014.
 */

public class LoginPresenter extends MvpNullObjectBasePresenter<LoginView> {


    //环信相关
    private String hxPassword;

    private Call call;

    @Override
    public void attachView(LoginView view) {
        super.attachView(view);
        EventBus.getDefault().register(this);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
        EventBus.getDefault().unregister(this);
    }

    public void login(String username, String password) {
        hxPassword = password;
        getView().showPrb();
        call = EasyShopClient.getInstance().login(username, password);
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().hidePrb();
                getView().showMsg(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                UserResult userResult = new Gson().fromJson(body, UserResult.class);
                if (userResult.getCode() == 1) {
                    User user = userResult.getData();
                    //用户信息保存到本地配置中
                    CachePreferences.setUser(user);

                    //执行环信的登录相关
                    EaseUser easeUser = CurrentUser.convert(user);
                    HxUserManager.getInstance().asyncLogin(easeUser,hxPassword);
                } else if (userResult.getCode() == 2) {
                    getView().hidePrb();
                    getView().showMsg(userResult.getMessage());
                    getView().loginFailed();
                } else {
                    getView().hidePrb();
                    getView().showMsg("未知错误！");
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxSimpleEvent event){
        //判断是否是登录成功事件
        if (event.type != HxEventType.LOGIN) return;

        hxPassword = null;
        //调用登录成功的方法
        getView().loginSuccess();
        getView().showMsg("登录成功");

        EventBus.getDefault().post(new UserResult());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxErrorEvent event){
        //判断是否是登录成功事件
        if (event.type != HxEventType.LOGIN) return;

        hxPassword = null;
        getView().hidePrb();
        getView().showMsg(event.toString());
    }
}
