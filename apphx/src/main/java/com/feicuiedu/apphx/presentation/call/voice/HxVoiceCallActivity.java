package com.feicuiedu.apphx.presentation.call.voice;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.feicuiedu.apphx.R;
import com.feicuiedu.apphx.presentation.call.HxCallBaseActivity;
import com.hyphenate.chat.EMCallStateChangeListener.CallState;
import com.hyphenate.chat.EMCallStateChangeListener.CallError;
import com.hyphenate.easeui.utils.EaseUserUtils;

/**
 * 语音通话页面
 */
public class HxVoiceCallActivity extends HxCallBaseActivity implements HxVoiceCallView {

    private static final String EXTRA_KEY_HX_ID = "EXTRA_KEY_HX_ID";
    private static final String EXTRA_KEY_IS_INCOMING = "EXTRA_KEY_IS_INCOMING";

    public static Intent getStartIntent(Context context, String hxId, boolean isIncoming) {
        Intent intent = new Intent(context, HxVoiceCallActivity.class);
        intent.putExtra(EXTRA_KEY_HX_ID, hxId);
        intent.putExtra(EXTRA_KEY_IS_INCOMING, isIncoming);
        return intent;
    }

    private TextView tvCallState;
    private TextView tvMute;
    private TextView tvSpeaker;
    private Button btnAnswer;
    private Button btnRefuse;
    private Button btnHangup;

    private HxVoiceCallPresenter presenter;

    private String hxId;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hxId = getIntent().getStringExtra(EXTRA_KEY_HX_ID);
        boolean isIncoming = getIntent().getBooleanExtra(EXTRA_KEY_IS_INCOMING, true);

        setContentView(R.layout.activity_hx_voice_call);

        presenter = new HxVoiceCallPresenter();
        presenter.onCreate();
        presenter.attachView(this);
        presenter.voiceCall(isIncoming, hxId);
    }


    @Override public void onContentChanged() {
        super.onContentChanged();
        showUserInfo(hxId);
        findViews();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter.onDestroy();
    }

    @Override public void onBackPressed() {
    }

    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.text_mute) {
            presenter.setMute(!v.isSelected());
        } else if (viewId == R.id.text_hands_free) {
            presenter.setSpeakerOn(!v.isSelected());
        } else if (viewId == R.id.button_answer) {
            presenter.answerCall();
        } else if (viewId == R.id.button_hangup) {
            presenter.endCall();
        } else if (viewId == R.id.button_refuse) {
            presenter.refuseCall();
        }
    }

    // start-interface: HxVoiceCallView
    @Override public void showMute(boolean mute) {
        tvMute.setSelected(mute);
    }

    @Override public void showSpeakerOn(boolean speakerOn) {
        tvSpeaker.setSelected(speakerOn);
    }

    @Override
    public void showCallState(CallState callState, CallError callError) {

        boolean hasError = (callError != null) && (callError != CallError.ERROR_NONE);
        String info = hasError ? callState.toString() + ": error is " + callError.toString() : callState.toString();
        tvCallState.setText(info);
    }


    @Override public void changeUiMode(boolean isInComing) {

        int visibility = isInComing? View.INVISIBLE : View.VISIBLE;
        tvMute.setVisibility(visibility);
        tvSpeaker.setVisibility(visibility);
        btnHangup.setVisibility(visibility);

        visibility = isInComing? View.VISIBLE : View.INVISIBLE;
        btnRefuse.setVisibility(visibility);
        btnAnswer.setVisibility(visibility);
    }

    @Override public void close() {
        finish();
    }// end-interface: HxVoiceCallView


    private void showUserInfo(String hxId) {
        ImageView ivAvatar = (ImageView) findViewById(R.id.image_avatar);
        TextView tvName = (TextView) findViewById(R.id.text_name);
        EaseUserUtils.setUserAvatar(this, hxId, ivAvatar);
        EaseUserUtils.setUserNick(hxId, tvName);
    }

    private void findViews(){
        tvCallState = (TextView) findViewById(R.id.text_call_state);
        tvMute = (TextView) findViewById(R.id.text_mute);
        tvSpeaker = (TextView) findViewById(R.id.text_hands_free);
        btnAnswer = (Button) findViewById(R.id.button_answer);
        btnRefuse = (Button) findViewById(R.id.button_refuse);
        btnHangup = (Button) findViewById(R.id.button_hangup);
    }
}
