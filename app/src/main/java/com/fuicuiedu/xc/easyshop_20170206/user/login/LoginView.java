package com.fuicuiedu.xc.easyshop_20170206.user.login;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Administrator on 2017/2/14 0014.
 */

public interface LoginView extends MvpView {

    void showPrb();

    void hidePrb();

    void loginFailed();

    void loginSuccess();

    void showMsg(String msg);
}
