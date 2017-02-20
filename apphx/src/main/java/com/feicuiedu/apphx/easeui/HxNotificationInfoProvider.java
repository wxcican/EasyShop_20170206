package com.feicuiedu.apphx.easeui;


import android.content.Context;
import android.content.Intent;

import com.feicuiedu.apphx.R;
import com.feicuiedu.apphx.presentation.chat.HxChatActivity;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.utils.EaseCommonUtils;

/**
 * 参考<a href="http://docs.easemob.com/im/200androidclientintegration/135easeuiuseguide">EaseUI 使用指南</a>中的：
 * 设置通知栏内容提供者。
 * <p/>
 * 注意此处的Provider不是ContentProvider，只是对EaseUI进行配置的一个接口。
 */
public class HxNotificationInfoProvider implements EaseNotifier.EaseNotificationInfoProvider {

    private final Context appContext;

    public HxNotificationInfoProvider(Context context) {
        appContext = context.getApplicationContext();
    }

    @Override public String getDisplayedText(EMMessage message) {
        // 设置状态栏的消息提示，可以根据message的类型做相应提示
        String ticker = EaseCommonUtils.getMessageDigest(message, appContext);
        if (message.getType() == EMMessage.Type.TXT) {
            ticker = ticker.replaceAll("\\[.{2,3}\\]", appContext.getString(R.string.hx_notify_expression));
        }

        EaseUser user = getEaseUser(message.getUserName());

        if (user != null) {
            return user.getNick() + ": " + ticker;
        } else {
            return message.getFrom() + ": " + ticker;
        }
    }

    @Override public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
        return null;
    }

    @Override public String getTitle(EMMessage message) {
        // 设置通知的标题，这里使用默认

        EaseUser easeUser = getEaseUser(message.getUserName());
        return easeUser == null ? message.getFrom() : easeUser.getNick();
    }

    @Override public int getSmallIcon(EMMessage message) {
        // 设置小图标，这里使用默认
        return 0;
    }

    @Override public Intent getLaunchIntent(EMMessage message) {
        //设置点击通知栏跳转事件
        return HxChatActivity.getStartIntent(appContext, message.getUserName());
    }

    private EaseUser getEaseUser(String hxId) {
        EaseUI.EaseUserProfileProvider provider = EaseUI.getInstance().getUserProfileProvider();
        return provider == null ? null : provider.getUser(hxId);
    }
}
