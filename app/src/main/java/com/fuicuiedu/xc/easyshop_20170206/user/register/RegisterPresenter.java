package com.fuicuiedu.xc.easyshop_20170206.user.register;

import com.fuicuiedu.xc.easyshop_20170206.model.User;
import com.fuicuiedu.xc.easyshop_20170206.model.UserResult;
import com.fuicuiedu.xc.easyshop_20170206.network.EasyShopClient;
import com.fuicuiedu.xc.easyshop_20170206.network.UICallBack;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/14 0014.
 */

public class RegisterPresenter extends MvpNullObjectBasePresenter<RegisterView> {
    //业务，执行网络请求，完成注册
    //在特定的地方，触发对应Ui操作

    private Call call;

    //视图销毁，取消网络请求
    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
    }

    public void register(String username, String password) {
        //显示加载动画
        getView().showPrb();
        call = EasyShopClient.getInstance().register(username, password);
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                //隐藏加载动画
                getView().hidePrb();
                //显示异常信息
                getView().showMsg(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                //隐藏进度条
                getView().hidePrb();
                //拿到返回结果（Gson）
                UserResult result = new Gson().fromJson(body, UserResult.class);
                //根据不同的结果码处理
                if (result.getCode() == 1) {
                    //成功提示
                    getView().showMsg("注册成功");
                    //拿到用户的实体类
                    User user = result.getData();
                    // TODO: 2017/2/14 0014  将用户的信息保存到本地配置中

                    //执行注册成功的方法
                    getView().registerSuccess();
                } else if (result.getCode() == 2) {
                    //提示失败信息
                    getView().showMsg(result.getMessage());
                    //执行注册失败的方法
                    getView().registerFailed();
                } else {
                    getView().showMsg("未知错误！");
                }
            }
        });
    }
}
