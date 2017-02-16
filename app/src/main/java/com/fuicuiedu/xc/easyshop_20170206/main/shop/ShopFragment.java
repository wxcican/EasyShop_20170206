package com.fuicuiedu.xc.easyshop_20170206.main.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fuicuiedu.xc.easyshop_20170206.R;
import com.fuicuiedu.xc.easyshop_20170206.commons.ActivityUtils;
import com.fuicuiedu.xc.easyshop_20170206.main.shop.details.GoodsDetailActivity;
import com.fuicuiedu.xc.easyshop_20170206.model.GoodsInfo;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 市场页面
 */

public class ShopFragment extends MvpFragment<ShopView,ShopPresenter> implements ShopView{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;//展示商品的列表
    @BindView(R.id.refreshLayout)
    PtrClassicFrameLayout refreshLayout;//刷新加载的控件
    @BindView(R.id.tv_load_error)
    TextView tvLoadError;//错误提示

    private ActivityUtils activityUtils;
    private ShopAdapter shopAdapter;

    //获取商品时，商品类型，获取全部商品时为空
    private String pageType = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        shopAdapter = new ShopAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public ShopPresenter createPresenter() {
        return new ShopPresenter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    //初始化视图
    private void initView() {
        //初始化RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        //给adapter添加跳转监听事件
        shopAdapter.setListener(new ShopAdapter.onItemClickListener() {
            @Override
            public void onItemClicked(GoodsInfo goodsInfo) {
                //跳转到详情页
                Intent intent = GoodsDetailActivity.getStartIntent(getContext(),goodsInfo.getUuid(),0);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(shopAdapter);

        //初始化RefreshLayout
        //使用本对象作为key，用来记录上一次刷新的事件，如果两次下拉刷新间隔太近，不会触发刷新方法
        refreshLayout.setLastUpdateTimeRelateObject(this);
        //设置刷新时显示的背景色
        refreshLayout.setBackgroundResource(R.color.recycler_bg);
        //关闭header所耗时长
        refreshLayout.setDurationToCloseHeader(1500);
        //实现刷新，加载回调
        refreshLayout.setPtrHandler(new PtrDefaultHandler2() {
            //加载更多时触发
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                presenter.loadData(pageType);
            }
            //刷新时触发
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                presenter.refreshData(pageType);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //当前页面没数据，刷新
        if (shopAdapter.getItemCount() == 0){
            refreshLayout.autoRefresh();
        }
    }

    //点击错误视图时刷数据
    @OnClick(R.id.tv_load_error)
    public void onClick(){
        //自动刷新
        refreshLayout.autoRefresh();
    }


    // ##########################   视图接口相关实现   ########################

    @Override
    public void showRefresh() {
        tvLoadError.setVisibility(View.GONE);
    }

    @Override
    public void showRefreshError(String msg) {
        //停止刷新
        refreshLayout.refreshComplete();
        //判断是否拿到数据
        if (shopAdapter.getItemCount() > 0){
            activityUtils.showToast(msg);
            return;
        }
        //显示加载错误提示
        tvLoadError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRefreshEnd() {
        activityUtils.showToast(getResources().getString(R.string.refresh_more_end));
        //停止刷新
        refreshLayout.refreshComplete();
    }

    @Override
    public void hideRefresh() {
        //停止刷新
        refreshLayout.refreshComplete();
    }

    @Override
    public void showLoadMoreLoading() {
        tvLoadError.setVisibility(View.GONE);
    }

    @Override
    public void showLoadMoreError(String msg) {
        //停止刷新
        refreshLayout.refreshComplete();
        //判断是否拿到数据
        if (shopAdapter.getItemCount() > 0){
            activityUtils.showToast(msg);
            return;
        }
        //显示加载错误提示
        tvLoadError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadMoreEnd() {
        activityUtils.showToast(getResources().getString(R.string.load_more_end));
        refreshLayout.refreshComplete();
    }

    @Override
    public void hideLoadMore() {
        refreshLayout.refreshComplete();
    }

    @Override
    public void addMoreData(List<GoodsInfo> data) {
        shopAdapter.addData(data);
    }

    @Override
    public void addRefreshData(List<GoodsInfo> data) {
        //数据清空
        shopAdapter.clear();
        if (data !=null){
            shopAdapter.addData(data);
        }
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }
}
