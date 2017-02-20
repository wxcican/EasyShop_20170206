package com.feicuiedu.apphx.presentation.chat;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.feicuiedu.apphx.Preconditions;
import com.feicuiedu.apphx.R;
import com.feicuiedu.apphx.model.HxContactManager;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;

/**
 * 聊天Activity，此Activity是一个简单的Toolbar + Fragment结构，主要功能由
 * {@link HxChatFragment}实现。
 * <p/>
 * 启动此Activity时，请使用{@link #getStartIntent(Context, String)}方法，
 * 避免忘记传递必要的参数。
 */
public class HxChatActivity extends AppCompatActivity {

    /**
     * @param context 上下文对象
     * @param chatId  对方的环信Id
     * @return 启动聊天Activity的Intent
     */
    public static Intent getStartIntent(Context context, @NonNull String chatId) {
        Preconditions.checkNotNull(chatId, "Intent bundle should contain chatId argument!");

        Intent intent = new Intent(context, HxChatActivity.class);

        // Note: 设置聊天类型，我们这里只处理了单聊，没有处理群组和聊天室的情况。
        intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        intent.putExtra(EaseConstant.EXTRA_USER_ID, chatId);

        // 一旦进入聊天页面，就取消通知栏通知
        EaseUI.getInstance()
                .getNotifier()
                .reset();

        return intent;
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hx_chat);
    }

    @Override public void onContentChanged() {
        super.onContentChanged();

        initToolbar();
        addChatFragment();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 初始化Toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String chatId = getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);
        EaseUser easeUser = HxContactManager.getInstance()
                .getUser(chatId);

        String title = easeUser.getNick();
        if (!TextUtils.isEmpty(title)) {
            getSupportActionBar().setTitle(title);
        }

    }

    // 添加聊天Fragment
    private void addChatFragment() {
        int chatType = getIntent().getIntExtra(EaseConstant.EXTRA_CHAT_TYPE, 0);
        String chatId = getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);

        HxChatFragment hxChatFragment = HxChatFragment.getInstance(chatType, chatId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.layout_container, hxChatFragment)
                .commit();
    }

}
