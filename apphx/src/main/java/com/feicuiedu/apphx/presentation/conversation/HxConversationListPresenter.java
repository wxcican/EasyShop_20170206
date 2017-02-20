package com.feicuiedu.apphx.presentation.conversation;


import android.support.annotation.NonNull;

import com.feicuiedu.apphx.basemvp.MvpPresenter;
import com.feicuiedu.apphx.model.HxMessageManager;
import com.feicuiedu.apphx.model.event.HxNewMsgEvent;
import com.feicuiedu.apphx.model.event.HxRefreshContactEvent;
import com.hyphenate.chat.EMConversation;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HxConversationListPresenter extends MvpPresenter<HxConversationListView> {


    @NonNull @Override protected HxConversationListView getNullObject() {
        return HxConversationListView.NULL;
    }

    public void deleteConversation(EMConversation conversation, boolean deleteMessage) {
        HxMessageManager.getInstance().deleteConversation(conversation.getUserName(), deleteMessage);
        getView().refreshConversations();
    }

    @SuppressWarnings("UnusedParameters")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxNewMsgEvent event) {
        // 收到新消息时，自动刷新会话列表
        getView().refreshConversations();
    }

    @SuppressWarnings("UnusedParameters")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxRefreshContactEvent event) {
        // 删除好友时，需要刷新会话列表
        getView().refreshConversations();
    }
}
