package com.feicuiedu.apphx.presentation.contact.invitation;


import android.support.annotation.NonNull;

import com.feicuiedu.apphx.basemvp.MvpPresenter;
import com.feicuiedu.apphx.model.HxContactManager;
import com.feicuiedu.apphx.model.entity.InviteMessage;
import com.feicuiedu.apphx.model.event.HxErrorEvent;
import com.feicuiedu.apphx.model.event.HxEventType;
import com.feicuiedu.apphx.model.event.HxRefreshInviteEvent;
import com.feicuiedu.apphx.model.event.HxSimpleEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import timber.log.Timber;

public class HxInvitationsPresenter extends MvpPresenter<HxInvitationsView> {

    private final HxContactManager hxContactManager;

    public HxInvitationsPresenter() {
        hxContactManager = HxContactManager.getInstance();
    }

    @NonNull @Override protected HxInvitationsView getNullObject() {
        return HxInvitationsView.NULL;
    }

    public void getInvites() {
        hxContactManager.getInvites();
    }

    public void accept(InviteMessage invite) {
        hxContactManager.asyncAcceptInvite(invite);
    }

    public void refuse(InviteMessage invite) {
        hxContactManager.asyncRefuseInvite(invite);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxRefreshInviteEvent event) {
        Timber.d("onEvent HxRefreshInviteEvent");
        getView().refreshInvitations(event.messages);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxSimpleEvent event) {
        if (event.type == HxEventType.ACCEPT_INVITE || event.type == HxEventType.REFUSE_INVITE) {
            // 同意或拒绝邀请成功
            getView().refreshInvitations();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxErrorEvent event) {
        if (event.type == HxEventType.ACCEPT_INVITE || event.type == HxEventType.REFUSE_INVITE) {
            // 同意或拒绝邀请失败
            getView().showActionFail();
        }
    }

}
