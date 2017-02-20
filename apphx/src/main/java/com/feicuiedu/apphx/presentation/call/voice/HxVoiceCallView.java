package com.feicuiedu.apphx.presentation.call.voice;


import com.feicuiedu.apphx.presentation.call.HxCallBaseView;
import com.hyphenate.chat.EMCallStateChangeListener.CallState;
import com.hyphenate.chat.EMCallStateChangeListener.CallError;

public interface HxVoiceCallView extends HxCallBaseView{


    void showMute(boolean mute);

    void showSpeakerOn(boolean speakerOn);

    void showCallState(CallState callState, CallError callError);

    void changeUiMode(boolean isIncoming);

    void close();


    HxVoiceCallView NULL = new HxVoiceCallView() {
        @Override public void showMute(boolean mute) {
        }

        @Override public void showSpeakerOn(boolean speakerOn) {
        }

        @Override public void showCallState(CallState callState, CallError callError) {
        }

        @Override public void changeUiMode(boolean isIncoming) {
        }

        @Override public void close() {
        }

        @Override public void playRingtone() {
        }

        @Override public void stopRingtone() {
        }

        @Override public void setSpeakerOn(boolean speakerOn) {
        }
    };
}
