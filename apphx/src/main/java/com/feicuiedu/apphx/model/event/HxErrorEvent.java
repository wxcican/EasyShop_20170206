package com.feicuiedu.apphx.model.event;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

/**
 * 环信操作发生错误的事件
 * <p/>
 * 环信Sdk提供的方法有两种：同步方法(例如{@link EMClient#logout(boolean)})
 * 和异步方法(例如{@link EMClient#logout(boolean, EMCallBack)})。
 * <ul>
 * <li/>同步方法发生错误，会抛出{@link HyphenateException}异常；
 * <li/>异步方法发生错误，会有错误回调，给出错误码和错误信息。
 * </ul>
 */
public final class HxErrorEvent {

    public final HxEventType type;

    private final int errorCode;

    private final String errorMessage;

    public HxErrorEvent(HxEventType type, HyphenateException e) {
        this.type = type;
        this.errorCode = e.getErrorCode();
        this.errorMessage = e.getDescription();
    }

    @SuppressWarnings("SameParameterValue")
    public HxErrorEvent(HxEventType type, int errorCode, String errorMessage) {
        this.type = type;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override public String toString() {
        return String.format("code: %s %s", errorCode, errorMessage);
    }
}
