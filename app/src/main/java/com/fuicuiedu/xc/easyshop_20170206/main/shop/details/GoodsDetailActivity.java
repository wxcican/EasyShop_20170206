package com.fuicuiedu.xc.easyshop_20170206.main.shop.details;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuicuiedu.xc.easyshop_20170206.R;
import com.fuicuiedu.xc.easyshop_20170206.commons.ActivityUtils;
import com.fuicuiedu.xc.easyshop_20170206.components.AvatarLoadOptions;
import com.fuicuiedu.xc.easyshop_20170206.components.ProgressDialogFragment;
import com.fuicuiedu.xc.easyshop_20170206.model.CachePreferences;
import com.fuicuiedu.xc.easyshop_20170206.model.GoodsDetail;
import com.fuicuiedu.xc.easyshop_20170206.model.User;
import com.fuicuiedu.xc.easyshop_20170206.network.EasyShopApi;
import com.fuicuiedu.xc.easyshop_20170206.user.login.LoginActivity;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

public class GoodsDetailActivity extends MvpActivity<GoodsDetailView,GoodsDetailPresenter> implements GoodsDetailView {

    private static final String UUID = "uuid";
    //从不同的页面进入详情页的状态值，0=从市场页面，1=从我的商品页面
    private static final String STATE = "state";

    public static Intent getStartIntent(Context context,String uuid,int state){
        Intent intent = new Intent(context,GoodsDetailActivity.class);
        intent.putExtra(UUID,uuid);
        intent.putExtra(STATE,state);
        return intent;
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    /*使用开源库CircleIndicator来实现ViewPager的圆点指示器。*/
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.tv_detail_name)
    TextView tv_detail_name;
    @BindView(R.id.tv_detail_price)
    TextView tv_detail_price;
    @BindView(R.id.tv_detail_master)
    TextView tv_detail_master;
    @BindView(R.id.tv_detail_describe)
    TextView tv_detail_describe;
    @BindView(R.id.tv_goods_delete)
    TextView tv_goods_delete;
    @BindView(R.id.tv_goods_error)
    TextView tv_goods_error;
    @BindView(R.id.btn_detail_message)
    Button btn_detail_message;

    private String str_uuid;
    private ArrayList<ImageView> list;
    private ArrayList<String> list_uri;//存放图片路径的集合
    private GoodsDetailAdapter adapter;//viewPager的适配器
    private ActivityUtils activityUtils;
    private User goods_user;
    private ProgressDialogFragment progressDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);

        //左上角添加返回按钮
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activityUtils = new ActivityUtils(this);

        list = new ArrayList<>();
        list_uri = new ArrayList<>();
        adapter = new GoodsDetailAdapter(list);
        adapter.setListener(new GoodsDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                //点击图片，跳转到图片详情页
                Intent intent = GoodsDetailInfoActivity.getStartIntent(GoodsDetailActivity.this,list_uri);
                startActivity(intent);
            }
        });
        viewPager.setAdapter(adapter);

        init();//初始化视图

    }

    //初始化视图
    private void init() {
        //拿到uuid（商品主键）
        str_uuid = getIntent().getStringExtra(UUID);
        //来自哪个页面，0=从市场页面，1=从我的商品页面
        int btn_show = getIntent().getIntExtra(STATE,0);
        //如果=1，来自我的页面
        if(btn_show == 1){
            tv_goods_delete.setVisibility(View.VISIBLE);//显示“删除”
            btn_detail_message.setVisibility(View.GONE);//隐藏“发消息“
        }
        presenter.getData(str_uuid);//获取商品详情，业务
    }

    //点击事件，发消息，删除
    @OnClick({R.id.btn_detail_message,R.id.tv_goods_delete})
    public void onClick(View view){
        //判断登录状态
        if (CachePreferences.getUser().getName() == null){
            activityUtils.startActivity(LoginActivity.class);
            return;
        }
        switch (view.getId()){
            //发消息
            case R.id.btn_detail_message:
                // TODO: 2017/2/16 0016 跳转环信发消息的页面
                activityUtils.showToast("跳转环信发消息的页面,待实现");
                break;
            //删除
            case R.id.tv_goods_delete:
                // TODO: 2017/2/16 0016 删除相关
                activityUtils.showToast("删除相关,待实现");
                break;
        }
    }


    @NonNull
    @Override
    public GoodsDetailPresenter createPresenter() {
        return new GoodsDetailPresenter();
    }

    //左上角返回，需实现的方法
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }


    //  ########################    视图接口相关方法   #####################
    @Override
    public void showProgress() {
        if (progressDialogFragment == null) progressDialogFragment = new ProgressDialogFragment();
        if (progressDialogFragment.isVisible()) return;
        progressDialogFragment.show(getSupportFragmentManager(),"fragment_progress_dialog");
    }

    @Override
    public void hideProgress() {
        progressDialogFragment.dismiss();
    }

    @Override
    public void setImageData(ArrayList<String> arrayList) {
        list_uri = arrayList;
        //加载图片
        for (int i = 0; i < list_uri.size(); i++) {
            ImageView imageView = new ImageView(this);
            ImageLoader.getInstance()
                    .displayImage(EasyShopApi.IMAGE_URL + list_uri.get(i)
                    ,imageView, AvatarLoadOptions.build_item());
            //添加到imageView的集合中
            list.add(imageView);
        }
        adapter.notifyDataSetChanged();
        //确认图片数量后，创建圆点指示器
        indicator.setViewPager(viewPager);
    }

    @Override
    public void setData(GoodsDetail data, User goods_user) {
        //数据展示
        this.goods_user = goods_user;
        tv_detail_name.setText(data.getName());
        tv_detail_price.setText(getString(R.string.goods_money, data.getPrice()));
        tv_detail_master.setText(getString(R.string.goods_detail_master, goods_user.getNick_name()));
        tv_detail_describe.setText(data.getDescription());
    }

    @Override
    public void showError() {
        tv_goods_error.setVisibility(View.VISIBLE);
        toolbar.setTitle(R.string.goods_overdue);
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void deleteEnd() {
        finish();
    }
}
