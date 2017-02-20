package com.feicuiedu.apphx.presentation.call;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.feicuiedu.apphx.Preconditions;
import com.feicuiedu.apphx.model.HxUserManager;
import com.feicuiedu.apphx.presentation.call.voice.HxVoiceCallActivity;

public class CallReceiver extends BroadcastReceiver{

    private static final String EXTRA_KEY_FROM = "EXTRA_KEY_FROM";
    private static final String EXTRA_KEY_TYPE = "EXTRA_KEY_TYPE";

    private static final String CALL_TYPE_VIDEO = "video";

    @Override public void onReceive(Context context, Intent intent) {
        Preconditions.checkArgument(HxUserManager.getInstance().isLogin());

        String hxId = intent.getStringExtra(EXTRA_KEY_FROM);
        String type = intent.getStringExtra(EXTRA_KEY_TYPE);

        if (CALL_TYPE_VIDEO.equals(type)) {
            // TODO: Video Call
        } else {
            Intent voiceCall = HxVoiceCallActivity.getStartIntent(context, hxId, true);
            voiceCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(voiceCall);
        }
    }
}
