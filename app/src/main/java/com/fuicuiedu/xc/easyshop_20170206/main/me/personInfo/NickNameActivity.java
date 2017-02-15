package com.fuicuiedu.xc.easyshop_20170206.main.me.personInfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.fuicuiedu.xc.easyshop_20170206.R;
import com.fuicuiedu.xc.easyshop_20170206.commons.ActivityUtils;
import com.fuicuiedu.xc.easyshop_20170206.commons.RegexUtils;
import com.fuicuiedu.xc.easyshop_20170206.model.CachePreferences;
import com.fuicuiedu.xc.easyshop_20170206.model.User;
import com.fuicuiedu.xc.easyshop_20170206.model.UserResult;
import com.fuicuiedu.xc.easyshop_20170206.network.EasyShopClient;
import com.fuicuiedu.xc.easyshop_20170206.network.UICallBack;
import com.google.gson.Gson;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

public class NickNameActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_nickname)
    EditText etNickName;

    private ActivityUtils activityUtils;
    private String str_nickname;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name);
        unbinder = ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activityUtils = new ActivityUtils(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_save)
    public void onClick() {
        //拿到用户输入的昵称
        str_nickname = etNickName.getText().toString();
        //判断昵称是否符合规则
        if (RegexUtils.verifyNickname(str_nickname) != RegexUtils.VERIFY_SUCCESS) {
            String msg = getString(R.string.nickname_rules);
            activityUtils.showToast(msg);
            return;
        }
        init();//修改昵称
    }

    //修改昵称
    private void init() {
        //从本地配置中拿到用户类
        User user = CachePreferences.getUser();
        //把要修改的昵称设置进来
        user.setNick_name(str_nickname);
        //执行修改昵称的网络请求
        Call call = EasyShopClient.getInstance().uploadUser(user);
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                activityUtils.showToast(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                UserResult result = new Gson().fromJson(body, UserResult.class);
                //如果修改失败，则跳出
                if (result.getCode() != 1) {
                    activityUtils.showToast(result.getMessage());
                    return;
                }
                //如果修改成功
                CachePreferences.setUser(result.getData());
                activityUtils.showToast("修改成功");
                //返回
                onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
