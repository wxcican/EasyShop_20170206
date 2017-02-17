package com.fuicuiedu.xc.easyshop_20170206.main.shop.details;

import com.fuicuiedu.xc.easyshop_20170206.model.GoodsDetail;
import com.fuicuiedu.xc.easyshop_20170206.model.GoodsDetailResult;
import com.fuicuiedu.xc.easyshop_20170206.network.EasyShopClient;
import com.fuicuiedu.xc.easyshop_20170206.network.UICallBack;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/2/16 0016.
 */

public class GoodsDetailPresenter extends MvpNullObjectBasePresenter<GoodsDetailView> {

    //删除商品相关
    private Call deleteCall;

    //获取详情的Call
    private Call getDetailCall;

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (getDetailCall != null) getDetailCall.cancel();
        if (deleteCall != null) deleteCall.cancel();
    }

    //获取商品的详细数据
    public void getData(String uuid) {
        getView().showProgress();
        getDetailCall = EasyShopClient.getInstance().getGoodsData(uuid);
        getDetailCall.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().hideProgress();
                getView().showMessage(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                getView().hideProgress();
                GoodsDetailResult goodsDetailResult = new Gson().fromJson(body, GoodsDetailResult.class);
                if (goodsDetailResult.getCode() == 1) {
                    //商品详情
                    GoodsDetail goodsDetail = goodsDetailResult.getDatas();
                    //用来存放图片路径集合
                    ArrayList<String> list = new ArrayList<String>();
                    for (int i = 0; i < goodsDetail.getPages().size(); i++) {
                        String page = goodsDetail.getPages().get(i).getUri();
                        list.add(page);
                    }
                    getView().setImageData(list);
                    getView().setData(goodsDetail, goodsDetailResult.getUser());
                } else {
                    getView().showError();
                }
            }
        });
    }

    //删除商品
    public void delete(String uuid){
        getView().showProgress();
        deleteCall = EasyShopClient.getInstance().deleteGoods(uuid);
        deleteCall.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().hideProgress();
                getView().showMessage(e.getMessage());
            }
            @Override
            public void onResponseUI(Call call, String body) {
                getView().hideProgress();
                GoodsDetailResult goodsDetailResult = new Gson().fromJson(body,GoodsDetailResult.class);
                if (goodsDetailResult.getCode() == 1){
                    //执行删除商品的方法
                    getView().deleteEnd();
                    getView().showMessage("删除成功");
                }else{
                    getView().showMessage("删除失败，未知错误");
                }
            }
        });

    }
}
