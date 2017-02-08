package com.fuicuiedu.xc.easyshop_20170206.main.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fuicuiedu.xc.easyshop_20170206.R;
import com.fuicuiedu.xc.easyshop_20170206.commons.ActivityUtils;
import com.fuicuiedu.xc.easyshop_20170206.user.LoginActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的页面
 */

public class MeFragment extends Fragment {

    private View view;
    private ActivityUtils activityUtils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_me,container,false);
            ButterKnife.bind(this,view);
            activityUtils = new ActivityUtils(this);
        }
        return view;
    }

    @OnClick({R.id.iv_user_head,R.id.tv_person_info, R.id.tv_login, R.id.tv_person_goods, R.id.tv_goods_upload})
    public void onClick(View view) {
        activityUtils.startActivity(LoginActivity.class);
        // TODO: 2017/2/7 0007 需要判断是否登录，从而决定跳转位置
    }
}
