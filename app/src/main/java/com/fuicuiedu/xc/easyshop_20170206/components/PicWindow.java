package com.fuicuiedu.xc.easyshop_20170206.components;//package com.fuicuiedu.xc.easyshop_test_20170206.components;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.fuicuiedu.xc.easyshop_20170206.R;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 自定义PopupWindow,用于图片的选择
 */
public class PicWindow extends PopupWindow {

    public interface Listener {
        /**
         * 来自相册
         */
        void toGallery();

        /**
         * 来自相机
         */
        void toCamera();
    }

    private final Activity activity;

    private final Listener listener;

    @SuppressWarnings("all")
    public PicWindow(Activity activity, Listener listener) {

        super(activity.getLayoutInflater().inflate(R.layout.layout_popu, null),
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        ButterKnife.bind(this, getContentView());
        this.activity = activity;
        this.listener = listener;

        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
    }

    public void show() {
        showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @OnClick({R.id.btn_popu_album, R.id.btn_popu_camera, R.id.btn_popu_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_popu_album:
                listener.toGallery();
                break;
            case R.id.btn_popu_camera:
                listener.toCamera();
                break;
            case R.id.btn_popu_cancel:
                break;
        }
        dismiss();
    }

}

