package com.fuicuiedu.xc.easyshop_20170206.user.register;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Administrator on 2017/2/14 0014.
 */

public interface RegisterView extends MvpView{

    void showPrb();

    void hidePrb();
    //注册成功
    void registerSuccess();
    //注册失败
    void registerFailed();

    void showMsg(String msg);
    //用户名密码不对是提示用户
    void showUserPasswordError(String msg);
}
