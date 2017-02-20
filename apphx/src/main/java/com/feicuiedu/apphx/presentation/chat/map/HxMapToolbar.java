package com.feicuiedu.apphx.presentation.chat.map;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.feicuiedu.apphx.R;
import com.hyphenate.easeui.ui.EaseBaiduMapActivity;

/**
 * 用于{@link EaseBaiduMapActivity}。
 * <p/>
 * 在此模块提供同名布局：<b>ease_activity_baidumap.xml</b>，覆盖EaseUI的布局效果，从而替换地图界面的标题栏。
 */
public class HxMapToolbar extends FrameLayout {
    public HxMapToolbar(Context context) {
        this(context, null);
    }

    public HxMapToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HxMapToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        LayoutInflater.from(context)
                .inflate(R.layout.partial_hx_map_toolbar, this, true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });
    }
}
