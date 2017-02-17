package com.fuicuiedu.xc.easyshop_20170206.main.me.persongoods;

import com.fuicuiedu.xc.easyshop_20170206.main.shop.ShopView;
import com.fuicuiedu.xc.easyshop_20170206.model.CachePreferences;
import com.fuicuiedu.xc.easyshop_20170206.model.GoodsDetail;
import com.fuicuiedu.xc.easyshop_20170206.model.GoodsResult;
import com.fuicuiedu.xc.easyshop_20170206.network.EasyShopClient;
import com.fuicuiedu.xc.easyshop_20170206.network.UICallBack;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/2/17 0017.
 */

public class PersonGoodsPresenter extends MvpNullObjectBasePresenter<ShopView>{

    private Call call;
    private int pageInt = 1;

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
    }

    //刷新数据
    public void refreshData(String type){
        getView().showRefresh();
        call = EasyShopClient.getInstance().getPersonData(1,type, CachePreferences.getUser().getName());
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().showRefreshError(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                GoodsResult goodsResult = new Gson().fromJson(body, GoodsResult.class);
                switch (goodsResult.getCode()){
                    case 1:
                        if (goodsResult.getDatas().size() == 0){
                            getView().showRefreshEnd();
                        }else{
                            getView().addRefreshData(goodsResult.getDatas());
                            getView().hideRefresh();
                        }
                        pageInt = 2;
                        break;
                    default:
                        getView().showRefreshError(goodsResult.getMessage());
                }
            }
        });
    }

    //加载数据
    public void loadData(String type) {
        getView().showLoadMoreLoading();
//        if (pageInt == 0) pageInt = 1;
        call = EasyShopClient.getInstance().getPersonData(pageInt, type, CachePreferences.getUser().getName());
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().showLoadMoreError(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                GoodsResult goodsResult = new Gson().fromJson(body, GoodsResult.class);
                switch (goodsResult.getCode()) {
                    case 1:
                        if (goodsResult.getDatas().size() == 0) {
                            getView().showLoadMoreEnd();
                        } else {
                            getView().addMoreData(goodsResult.getDatas());
                            getView().hideLoadMore();
                        }
                        pageInt++;
                        break;
                    default:
                        getView().showLoadMoreError(goodsResult.getMessage());
                }
            }
        });
    }
}
