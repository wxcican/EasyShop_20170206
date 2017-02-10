package com.fuicuiedu.xc.easyshop_20170206.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fuicuiedu.xc.easyshop_20170206.R;
import com.fuicuiedu.xc.easyshop_20170206.commons.ActivityUtils;
import com.fuicuiedu.xc.easyshop_20170206.commons.LogUtils;
import com.fuicuiedu.xc.easyshop_20170206.commons.RegexUtils;
import com.fuicuiedu.xc.easyshop_20170206.components.ProgressDialogFragment;
import com.fuicuiedu.xc.easyshop_20170206.network.EasyShopClient;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_username)
    EditText et_userName;
    @BindView(R.id.et_pwd)
    EditText et_pwd;
    @BindView(R.id.et_pwdAgain)
    EditText et_pwdAgain;
    @BindView(R.id.btn_register)
    Button btn_register;

    private String username;
    private String password;
    private String pwd_again;
    private ActivityUtils activityUtils;
    private ProgressDialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);

        init();
    }

    private void init() {
        //给左上角加一个返回图标，需要重写菜单点击事件，否则点击无效
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_userName.addTextChangedListener(textWatcher);
        et_pwd.addTextChangedListener(textWatcher);
        et_pwdAgain.addTextChangedListener(textWatcher);
    }

    //给左上角加一个返回图标，需要重写菜单点击事件，否则点击无效
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //如果点击的是返回键，则finish
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            username = et_userName.getText().toString();
            password = et_pwd.getText().toString();
            pwd_again = et_pwdAgain.getText().toString();
            boolean canLogin = !(TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(pwd_again));
            btn_register.setEnabled(canLogin);
        }
    };

    @OnClick(R.id.btn_register)
    public void onClick() {
        if (RegexUtils.verifyUsername(username) != RegexUtils.VERIFY_SUCCESS) {
            activityUtils.showToast(R.string.username_rules);
            return;
        } else if (RegexUtils.verifyPassword(password) != RegexUtils.VERIFY_SUCCESS) {
            activityUtils.showToast(R.string.password_rules);
            return;
        } else if (!TextUtils.equals(password, pwd_again)) {
            activityUtils.showToast(R.string.username_equal_pwd);
            return;
        }

        Call call = EasyShopClient.getInstance().register(username,password);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

//            {
//                "code": 1,
//                "msg": "succeed",
//                "data": {
//                    "username": "xc62",
//                    "name": "yt59856b15cf394e7b84a7d48447d16098",
//                    "uuid": "0F8EC12223174657B2E842076D54C361",
//                    "password": "123456"
//            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //网络连接成功
                //响应成功
                if (response.isSuccessful()){
                    //拿到响应体
                    ResponseBody responseBody = response.body();
                    //响应体转换为String
                    String json = responseBody.string();
                    try {
                        //解析，String转为json格式
                        JSONObject jsonObject = new JSONObject(json);
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        //判断是否注册成功
                        if (code == 1){
                            //拿到data数据
                            JSONObject data = jsonObject.getJSONObject("data");
                            String username = data.getString("username");
                            LogUtils.e("注册成功，用户名=" + username);
                        }

                        //注册失败
                        if (code == 2){
                            LogUtils.e("注册失败，" + msg);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
