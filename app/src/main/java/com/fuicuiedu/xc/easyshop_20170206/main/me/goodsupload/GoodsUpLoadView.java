package com.fuicuiedu.xc.easyshop_20170206.main.me.goodsupload;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Administrator on 2017/2/17 0017.
 */

public interface GoodsUpLoadView extends MvpView{

    void showPrb();
    void hidePrb();
    void upLoadSuccess();
    void showMsg(String msg);
}
