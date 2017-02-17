package com.fuicuiedu.xc.easyshop_20170206.main.me.goodsupload;

import com.fuicuiedu.xc.easyshop_20170206.model.GoodsUpLoad;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/2/17 0017.
 */

public class GoodsUpLoadPresenter extends MvpNullObjectBasePresenter<GoodsUpLoadView>{

    private Call call;

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
    }

//    //商品上传
//    public void upLoad(GoodsUpLoad goodsUpLoad, List<Image>){
//
//    }
}
