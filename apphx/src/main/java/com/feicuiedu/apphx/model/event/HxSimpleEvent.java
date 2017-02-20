package com.feicuiedu.apphx.model.event;

/**
 * 避免创建太多事件类，对于没有具体数据的简单事件(例如登录环信成功)，统一用此事件代表。
 */
public final class HxSimpleEvent {

    public final HxEventType type;

    public HxSimpleEvent(HxEventType type) {
        this.type = type;
    }
}
