package com.feicuiedu.apphx.model;

import com.feicuiedu.apphx.model.event.HxNewMsgEvent;
import com.feicuiedu.apphx.model.event.HxRefreshContactEvent;
import com.feicuiedu.apphx.model.repository.ILocalUsersRepo;
import com.google.gson.Gson;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class HxMessageManager implements EMMessageListener {

    private static final String CMD_ACTION_UPDATE_AVATAR = "CMD_ACTION_UPDATE_AVATAR";
    private static final String CMD_ATTRIBUTE_AVATAR = "AVATAR";

    private static HxMessageManager sInstance;

    public static HxMessageManager getInstance() {
        if (sInstance == null) {
            sInstance = new HxMessageManager();
        }
        return sInstance;
    }

    private final EventBus eventBus;
    private final EMChatManager emChatManager;
    private ILocalUsersRepo localUsersRepo;

    private HxMessageManager() {
        eventBus = EventBus.getDefault();
        emChatManager = EMClient.getInstance().chatManager();
        emChatManager.addMessageListener(this);
    }

    // start-interface: EMMessageListener
    // 接受消息接口，在接受到文本消息，图片，视频，语音，地理位置，文件这些消息体的时候，会通过此接口通知用户。
    @Override public void onMessageReceived(List<EMMessage> list) {

        try {
            /*接受EMMessage中会话发起人的环信ID*/
            String user = list.get(0).getStringAttribute("User");
            EaseUser easeUser = new Gson().fromJson(user, EaseUser.class);

            if (easeUser != null) {
                localUsersRepo.save(easeUser);
            }

        } catch (HyphenateException e) {
            e.printStackTrace();
        }

        eventBus.post(new HxNewMsgEvent(list));
    }

    // 这个接口只包含命令的消息体(透传消息)，包含命令的消息体通常不对用户展示。
    @Override public void onCmdMessageReceived(List<EMMessage> list) {
        for (EMMessage message : list) {
            handleAvatarUpdateMessage(message);
        }
    }

    /* no-op */
    @Override public void onMessageReadAckReceived(List<EMMessage> list) {
        // 接受到消息体的已读回执, 消息的接收方已经阅读此消息。
    }

    /* no-op */
    @Override public void onMessageDeliveryAckReceived(List<EMMessage> list) {
        // 收到消息体的发送回执，消息体已经成功发送到对方设备。
    }

    /* no-op */
    @Override public void onMessageChanged(EMMessage emMessage, Object o) {
        // 接受消息发生改变的通知，包括消息ID的改变。消息是改变后的消息。
    } // end-interface: EMMessageListener

    public void deleteConversation(String hxId, boolean deleteMessage) {
        // 此操作是删除本地数据库中的一条数据，不耗时，可以同步执行
        emChatManager.deleteConversation(hxId, deleteMessage);
    }

    /**
     * 当用户更改其头像后，要通过此方法将新头像的信息透传给他的所有好友。
     *
     * @param avatar 用户新头像的地址
     */
    public void sendAvatarUpdateMessage(String avatar) {
        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
        cmdMsg.setAttribute(CMD_ATTRIBUTE_AVATAR, avatar);
        cmdMsg.setChatType(EMMessage.ChatType.Chat);
        EMCmdMessageBody body = new EMCmdMessageBody(CMD_ACTION_UPDATE_AVATAR);
        cmdMsg.addBody(body);

        for (String id : HxContactManager.getInstance().getContacts()) {
            cmdMsg.setReceipt(id);
            // 此方法是异步的
            emChatManager.sendMessage(cmdMsg);
        }
    }

    public void init(ILocalUsersRepo localUsersRepo) {
        this.localUsersRepo = localUsersRepo;
    }

    // 处理好友头像更新的透传消息
    private void handleAvatarUpdateMessage(EMMessage message) {
        EMMessageBody body = message.getBody();

        // 判断是否是透传消息
        if (!(body instanceof EMCmdMessageBody)) return;

        String action = ((EMCmdMessageBody) body).action();

        // 判断此透传消息是否是头像更新消息
        if (!CMD_ACTION_UPDATE_AVATAR.equals(action)) return;

        // 获取对方的环信Id和新头像地址
        String fromId = message.getFrom();
        String avatar = message.getStringAttribute(CMD_ATTRIBUTE_AVATAR, null);

        EaseUser easeUser = localUsersRepo.getUser(fromId);

        // 将新的好友信息存入本地仓库，然后通知Presenter刷新UI
        if (easeUser != null) {
            easeUser.setAvatar(avatar);
            localUsersRepo.save(easeUser);

            HxRefreshContactEvent event = new HxRefreshContactEvent();
            eventBus.post(event);
        }
    }

}
