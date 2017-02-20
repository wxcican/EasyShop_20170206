package com.feicuiedu.apphx.presentation.contact.invitation;


import com.feicuiedu.apphx.basemvp.MvpView;
import com.feicuiedu.apphx.model.entity.InviteMessage;

import java.util.List;

public interface HxInvitationsView extends MvpView{

    // 更新数据并刷新列表
    void refreshInvitations(List<InviteMessage> messages);

    // 刷新列表
    void refreshInvitations();

    // 显示“同意”或“拒绝”邀请的操作失败
    void showActionFail();


    HxInvitationsView NULL = new HxInvitationsView() {
        @Override public void refreshInvitations(List<InviteMessage> messages) {
        }

        @Override public void refreshInvitations() {
        }

        @Override public void showActionFail() {
        }
    };
}
