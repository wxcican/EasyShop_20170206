package com.fuicuiedu.xc.easyshop_20170206.main.me.personInfo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.fuicuiedu.xc.easyshop_20170206.R;
import com.fuicuiedu.xc.easyshop_20170206.commons.ActivityUtils;
import com.fuicuiedu.xc.easyshop_20170206.components.PicWindow;
import com.fuicuiedu.xc.easyshop_20170206.components.ProgressDialogFragment;
import com.fuicuiedu.xc.easyshop_20170206.main.MainActivity;
import com.fuicuiedu.xc.easyshop_20170206.model.CachePreferences;
import com.fuicuiedu.xc.easyshop_20170206.model.ItemShow;
import com.fuicuiedu.xc.easyshop_20170206.model.User;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonActivity extends MvpActivity<PersonView, PersonPersenter> implements PersonView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_user_head)
    ImageView ivUserHead;//用户头像
    @BindView(R.id.listView)
    ListView listView;//显示用户名，昵称，环信ID的listView

    private ActivityUtils activityUtils;
    private ProgressDialogFragment progressDialogFragment;
    private List<ItemShow> list = new ArrayList<>();
    private PersonAdapter adapter;
    private PicWindow picWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);

        activityUtils = new ActivityUtils(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new PersonAdapter(list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);

        // TODO: 2017/2/14 0014       //获取用户头像
    }

    @NonNull
    @Override
    public PersonPersenter createPresenter() {
        return new PersonPersenter();
    }

    //方便修改完昵称，回来更改数据
    @Override
    protected void onStart() {
        super.onStart();
        list.clear();
        init();//数据初始化
        adapter.notifyDataSetChanged();
    }

    //数据初始化
    private void init() {
        User user = CachePreferences.getUser();
        list.add(new ItemShow(getResources().getString(R.string.username), user.getName()));
        list.add(new ItemShow(getResources().getString(R.string.nickname), user.getNick_name()));
        list.add(new ItemShow(getResources().getString(R.string.hx_id), user.getHx_Id()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                //用户名
                case 0:
                    activityUtils.showToast(getResources().getString(R.string.username_update));
                    break;
                //昵称
                case 1:
                    activityUtils.startActivity(NickNameActivity.class);
                    break;
                //环信ID
                case 2:
                    activityUtils.showToast(getResources().getString(R.string.id_update));
                    break;
            }
        }
    };

    @OnClick({R.id.btn_login_out,R.id.iv_user_head})
    public void onClick(View view){
        switch (view.getId()){
            //点击头像
            case R.id.iv_user_head:
                //头像来源选择（相册，拍照）
                //如果为空，创建实例（实现监听）
                if (picWindow == null){
                    picWindow = new PicWindow(this,listener);
                }
                //如果已经显示，则关闭
                if (picWindow.isShowing()){
                    picWindow.dismiss();
                    return;
                }
                picWindow.show();//显示来源选择框
                break;
            //点击退出登录
            case R.id.btn_login_out:
                //清空本地配置
                CachePreferences.clearAllData();
                //清除所有旧的Activity
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                // TODO: 2017/2/14 0014 退出环信相关
        }
    }

    //图片选择弹窗的自定义监听
    private PicWindow.Listener listener = new PicWindow.Listener() {
        @Override
        public void toGallery() {
            //从相册中选择
            activityUtils.showToast("从相册中选择");
        }

        @Override
        public void toCamera() {
            //从相机中选择
            activityUtils.showToast("从相机中选择");
        }
    };

    // ######################    视图接口相关    #####################
    @Override
    public void showPrb() {
        if (progressDialogFragment == null) progressDialogFragment = new ProgressDialogFragment();
        if (progressDialogFragment.isVisible()) return;
        progressDialogFragment.show(getSupportFragmentManager(),"progress_dialog_fragment");
    }

    @Override
    public void hidePrb() {
        progressDialogFragment.dismiss();
    }

    @Override
    public void showMsg(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void updataAvatar(String url) {
        // TODO: 2017/2/14 0014 头像加载操作
    }
}
