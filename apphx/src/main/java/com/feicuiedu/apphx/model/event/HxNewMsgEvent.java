package com.feicuiedu.apphx.model.event;


import com.hyphenate.chat.EMMessage;

import java.util.List;

public class HxNewMsgEvent {

    public final List<EMMessage> list;

    public HxNewMsgEvent(List<EMMessage> list) {
        this.list = list;
    }
}
