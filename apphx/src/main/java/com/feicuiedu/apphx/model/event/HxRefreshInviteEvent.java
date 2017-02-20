package com.feicuiedu.apphx.model.event;


import com.feicuiedu.apphx.model.entity.InviteMessage;

import java.util.List;

/**
 * 刷新邀请信息事件。
 */
public class HxRefreshInviteEvent {

    public final List<InviteMessage> messages;

    public HxRefreshInviteEvent(List<InviteMessage> messages) {
        this.messages = messages;
    }
}
