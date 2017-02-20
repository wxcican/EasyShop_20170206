package com.feicuiedu.apphx.presentation.call.voice;


import android.support.annotation.NonNull;

import com.feicuiedu.apphx.basemvp.MvpPresenter;
import com.feicuiedu.apphx.model.HxCallManager;
import com.feicuiedu.apphx.model.event.HxCallExceptionEvent;
import com.feicuiedu.apphx.model.event.HxCallStateEvent;
import com.hyphenate.chat.EMCallStateChangeListener.CallState;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import timber.log.Timber;

public class HxVoiceCallPresenter extends MvpPresenter<HxVoiceCallView> {

    private final HxCallManager hxCallManager;

    public HxVoiceCallPresenter() {
        hxCallManager = HxCallManager.getInstance();
    }

    @NonNull @Override protected HxVoiceCallView getNullObject() {
        return HxVoiceCallView.NULL;
    }

    public void voiceCall(boolean isInComing, String hxId) {
        getView().changeUiMode(isInComing);
        getView().playRingtone();

        if (!isInComing) {
            hxCallManager.asyncVoiceCall(hxId);
            getView().showCallState(CallState.CONNECTING, null);
        }
    }

    public void setMute(boolean mute) {
        hxCallManager.setMute(mute);
        getView().showMute(mute);
    }

    public void setSpeakerOn(boolean speakerOn) {
        getView().showSpeakerOn(speakerOn);
        getView().setSpeakerOn(speakerOn);
    }

    public void refuseCall() {
        getView().stopRingtone();
        hxCallManager.asyncRejectCall();

        getView().close();
    }


    public void answerCall() {
        getView().stopRingtone();
        getView().changeUiMode(false);

        getView().showSpeakerOn(false);
        getView().setSpeakerOn(false);

        hxCallManager.asyncAnswerCall();
    }

    public void endCall(){
        getView().stopRingtone();
        hxCallManager.asyncEndCall();

        getView().close();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxCallStateEvent event) {
        getView().showCallState(event.callState, event.callError);

        if (event.callState == CallState.DISCONNNECTED) {
            getView().close();
        } else if (event.callState == CallState.ACCEPTED) {
            getView().stopRingtone();
            getView().showSpeakerOn(false);
            getView().setSpeakerOn(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxCallExceptionEvent event) {
        Timber.d("HxCallExceptionEvent: %s" , event.toString());
        getView().close();
    }

}
