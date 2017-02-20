package com.fuicuiedu.xc.easyshop_20170206.main.me.goodsupload;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.fuicuiedu.xc.easyshop_20170206.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsUpLoadImageShowActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_goods_phone)
    ImageView ivGoodsPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_up_load_image_show);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //拿到图片
        Bitmap bitmap = getIntent().getParcelableExtra("images");
        //拿到宽、
        int width = getIntent().getIntExtra("width",0);
        //拿到高
        int height = getIntent().getIntExtra("height",0);

        ivGoodsPhone.setMaxWidth(width);
        ivGoodsPhone.setMaxHeight(height);
        ivGoodsPhone.setImageBitmap(bitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}
