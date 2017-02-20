package com.feicuiedu.apphx.model.event;


import com.hyphenate.chat.EMCallStateChangeListener;

public class HxCallStateEvent {

    public final EMCallStateChangeListener.CallState callState;
    public final EMCallStateChangeListener.CallError callError;


    public HxCallStateEvent(EMCallStateChangeListener.CallState callState, EMCallStateChangeListener.CallError callError) {
        this.callState = callState;
        this.callError = callError;
    }
}
