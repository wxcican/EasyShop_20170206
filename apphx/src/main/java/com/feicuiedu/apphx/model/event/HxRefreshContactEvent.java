package com.feicuiedu.apphx.model.event;


import com.hyphenate.easeui.domain.EaseUser;

import java.util.List;

/**
 * 刷新联系人事件。
 */
public final class HxRefreshContactEvent {

    // 联系人的环信id列表。
    public final List<EaseUser> contacts;

    // true代表联系人列表发生了变化
    public final boolean changed;

    public HxRefreshContactEvent(List<EaseUser> contacts) {
        this.contacts = contacts;
        this.changed = true;
    }

    /**
     * 无参构造方法代表联系人列表无变化，但联系人的具体信息(如头像)发生了变化。
     */
    public HxRefreshContactEvent() {
        this.contacts = null;
        this.changed = false;
    }
}
