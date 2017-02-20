package com.feicuiedu.apphx.presentation.chat;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.feicuiedu.apphx.R;
import com.feicuiedu.apphx.model.HxUserManager;
import com.feicuiedu.apphx.model.repository.DefaultLocalUsersRepo;
import com.feicuiedu.apphx.presentation.call.voice.HxVoiceCallActivity;
import com.google.gson.Gson;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;

/**
 * <a href="http://docs.easemob.com/im/200androidclientintegration/135easeuiuseguide">EaseUI 使用指南</a>
 */
public class HxChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper{

    private static final int ITEM_VIDEO_CALL = 4;

    public static HxChatFragment getInstance(int chatType, String chatId) {

        HxChatFragment chatFragment = new HxChatFragment();
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, chatType);
        args.putString(EaseConstant.EXTRA_USER_ID, chatId);
        chatFragment.setArguments(args);

        return chatFragment;
    }


    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customUi();
    }

    @Override public void onPause() {
        super.onPause();
        hideSoftKeyboard();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.fragment_hx_chat_video_call, menu);
    }

    @Override public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_audio_call) {
            String hxId = getArguments().getString(EaseConstant.EXTRA_USER_ID);
            Intent voiceCall = HxVoiceCallActivity.getStartIntent(getContext(), hxId, false);
            startActivity(voiceCall);
        } else if (item.getItemId() == R.id.menu_video_call) {
            // TODO: 视频通话功能
        } else {
            throw new RuntimeException("Wrong branch!");
        }

        return true;
    }

    /**
     * @see #onExtendMenuItemClick
     */
    @Override protected void registerExtendMenuItem() {
        // 替换EaseUI自带的图标
        itemdrawables[0] = R.drawable.btn_capture_picture;
        itemdrawables[1] = R.drawable.btn_select_picture;
        itemdrawables[2] = R.drawable.btn_location;

        // EaseUI已经设置了“相机”、“图片”和“位置”三个扩展菜单项
        super.registerExtendMenuItem();

        // 添加“视频通话”扩展菜单项
        inputMenu.registerExtendMenuItem(
                R.string.hx_chat_video_call,
                R.drawable.btn_video_call,
                ITEM_VIDEO_CALL,
                extendMenuItemClickListener
        );
    }

    // start-interface: EaseChatFragment.EaseChatFragmentHelper
    @Override public void onSetMessageAttributes(EMMessage message) {
        // 设置消息扩展属性
        /*发送消息时将会话发起人的环信ID放到消息EMMessage中*/
        String userId = HxUserManager.getInstance().getCurrentUserId();
        EaseUser easeUser = DefaultLocalUsersRepo.getInstance(getContext())
                .getUser(userId);

        message.setAttribute("User", new Gson().toJson(easeUser));
    }

    @Override public void onEnterToChatDetails() {
        // 进入会话详情
    }

    @Override public void onAvatarClick(String hxId) {
        // 用户头像点击事件
    }

    @Override public void onAvatarLongClick(String hxId) {
        // 用户头像长按事件
    }

    @Override public boolean onMessageBubbleClick(EMMessage message) {
        // 消息气泡框点击事件
        return false;
    }

    @Override public void onMessageBubbleLongClick(EMMessage message) {
        // 消息气泡框长按事件
    }

    /**
     * 扩展输入栏item点击事件,如果要覆盖EaseChatFragment已有的点击事件，return true
     */
    @Override public boolean onExtendMenuItemClick(int itemId, View view) {

        if (itemId == ITEM_VIDEO_CALL) {
            registerForContextMenu(view);
            getActivity().openContextMenu(view);
            return true;
        }
        return false;
    }

    @Override public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        // 设置自定义ChatRow提供者
        return null;
    } // end-interface: EaseChatFragment.EaseChatFragmentHelper

    private void customUi() {
        hideTitleBar();
        setChatFragmentListener(this);
        customSendButton();
    }

    // 发送按钮
    @SuppressWarnings("deprecation")
    private void customSendButton() {
        Button btnSend = (Button) inputMenu.findViewById(R.id.btn_send);
        btnSend.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_12));

        btnSend.setBackgroundResource(R.drawable.btn_send_bg);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnSend.setStateListAnimator(null);
        }

        btnSend.setTextColor(getResources().getColorStateList(R.color.selector_light_text));

        ViewGroup.LayoutParams params = btnSend.getLayoutParams();
        params.width = getResources().getDimensionPixelSize(R.dimen.size_48);
        btnSend.setLayoutParams(params);
    }
}
