package com.feicuiedu.apphx.presentation.conversation;


import com.feicuiedu.apphx.basemvp.MvpView;

public interface HxConversationListView extends MvpView{

    void refreshConversations();

    HxConversationListView NULL = new HxConversationListView() {
        @Override public void refreshConversations() {
        }
    };
}
