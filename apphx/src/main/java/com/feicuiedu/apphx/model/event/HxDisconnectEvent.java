package com.feicuiedu.apphx.model.event;

/**
 * 环信连接断开事件，参见{@link com.feicuiedu.apphx.model.HxUserManager#onDisconnected(int)}。
 * <p/>
 * 注意，如果是网络状态差等因素造成的连接断开，无需发送这一事件，环信Sdk会负责自动重连。
 * <p/>
 * 只在一些特殊情况下才发送这个事件，将用户踢出。目前处理的是：
 * <ul>
 * <li/> {@link com.hyphenate.EMError#USER_REMOVED} 服务器端删除此用户；
 * <li/> {@link com.hyphenate.EMError#USER_LOGIN_ANOTHER_DEVICE} 用户在其它设备登录。
 * </ul>
 */
public class HxDisconnectEvent {

    public final int errorCode;

    /**
     * @param errorCode 环信错误码，参见{@link com.hyphenate.EMError}
     */
    public HxDisconnectEvent(int errorCode) {
        this.errorCode = errorCode;
    }
}
