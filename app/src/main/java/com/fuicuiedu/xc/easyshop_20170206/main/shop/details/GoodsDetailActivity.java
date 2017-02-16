package com.fuicuiedu.xc.easyshop_20170206.main.shop.details;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuicuiedu.xc.easyshop_20170206.R;

import java.util.ArrayList;

import butterknife.BindView;
import me.relex.circleindicator.CircleIndicator;

public class GoodsDetailActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
    }
}
