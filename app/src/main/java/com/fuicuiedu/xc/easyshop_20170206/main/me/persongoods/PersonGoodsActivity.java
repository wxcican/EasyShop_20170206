package com.fuicuiedu.xc.easyshop_20170206.main.me.persongoods;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fuicuiedu.xc.easyshop_20170206.R;
import com.fuicuiedu.xc.easyshop_20170206.commons.ActivityUtils;
import com.fuicuiedu.xc.easyshop_20170206.main.shop.ShopAdapter;
import com.fuicuiedu.xc.easyshop_20170206.main.shop.ShopView;
import com.fuicuiedu.xc.easyshop_20170206.model.GoodsInfo;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class PersonGoodsActivity extends MvpActivity<ShopView,PersonGoodsPresenter> implements ShopView{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    /*下拉刷新和上拉加载的控件*/
    @BindView(R.id.refreshLayout)
    PtrClassicFrameLayout ptrFrameLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_load_error)
    TextView tv_load_error;
    @BindView(R.id.tv_load_empty)
    TextView tv_load_empty;

    @BindString(R.string.load_more_end)
    String load_more_end;

    private String pageType = "";//商品类型，空值为全部商品
    private ActivityUtils activityUtils;
    private ShopAdapter shopAdapter;//数据展示与市场页面相同，直接复用市场页面的适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_goods);
        ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);
        shopAdapter = new ShopAdapter();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //设置toolbar的监听事件
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        
        initView();//初始化视图


    }

    //初始化视图
    private void initView() {
        //初始化RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        shopAdapter.setListener(new ShopAdapter.onItemClickListener() {
            @Override
            public void onItemClicked(GoodsInfo goodsInfo) {
                // TODO: 2017/2/17 0017 我的商品详情页面
                activityUtils.showToast("我的商品详情页面，待实现");
            }
        });
        recyclerView.setAdapter(shopAdapter);

        //初始化RefreshLayout
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);
        ptrFrameLayout.setBackgroundResource(R.color.recycler_bg);
        ptrFrameLayout.setDurationToCloseHeader(1500);
        //刷新，加载回调
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                presenter.loadData(pageType);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                presenter.refreshData(pageType);
            }
        });
    }

    @NonNull
    @Override
    public PersonGoodsPresenter createPresenter() {
        return new PersonGoodsPresenter();
    }

    //每次进入本页面，如果没数据，自动刷新
    @Override
    protected void onStart() {
        super.onStart();
        if (shopAdapter.getItemCount() == 0){
            ptrFrameLayout.autoRefresh();
        }
    }

    //  ####################### toolbar 菜单选项相关   #########################
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //toolbar设置菜单选项
        getMenuInflater().inflate(R.menu.menu_goods_type,menu);
        return true;
    }

    //toobar菜单对应的单击事件
    private Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.menu_household:
                    presenter.refreshData("household");
                    break;
                case R.id.menu_electron:
                    presenter.refreshData("electron");
                    break;
                case R.id.menu_dress:
                    presenter.refreshData("dress");
                    break;
                case R.id.menu_book:
                    presenter.refreshData("book");
                    break;
                case R.id.menu_toy:
                    presenter.refreshData("toy");
                    break;
                case R.id.menu_gift:
                    presenter.refreshData("gift");
                    break;
                case R.id.menu_other:
                    presenter.refreshData("other");
                    break;
            }
            return false;
        }
    };

    //点击左上角返回，finish
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    //  ############################   视图接口相关   #########################

    @Override
    public void showRefresh() {
        tv_load_empty.setVisibility(View.GONE);
        tv_load_error.setVisibility(View.GONE);
    }

    @Override
    public void showRefreshError(String msg) {
        ptrFrameLayout.refreshComplete();
        if (shopAdapter.getItemCount() > 0){
            activityUtils.showToast(msg);
            return;
        }
        tv_load_error.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRefreshEnd() {
        ptrFrameLayout.refreshComplete();
        tv_load_empty.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRefresh() {
        ptrFrameLayout.refreshComplete();
    }

    @Override
    public void showLoadMoreLoading() {
        tv_load_error.setVisibility(View.GONE);
    }

    @Override
    public void showLoadMoreError(String msg) {
        ptrFrameLayout.refreshComplete();
        if (shopAdapter.getItemCount() > 0){
            activityUtils.showToast(msg);
            return;
        }
        tv_load_error.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadMoreEnd() {
        activityUtils.showToast(load_more_end);
        ptrFrameLayout.refreshComplete();
    }

    @Override
    public void hideLoadMore() {
        ptrFrameLayout.refreshComplete();
    }

    @Override
    public void addMoreData(List<GoodsInfo> data) {
        shopAdapter.addData(data);
    }

    @Override
    public void addRefreshData(List<GoodsInfo> data) {
        shopAdapter.clear();
        if (data != null)shopAdapter.addData(data);
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }
}
