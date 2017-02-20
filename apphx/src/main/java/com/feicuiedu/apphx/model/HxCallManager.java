package com.feicuiedu.apphx.model;


import com.feicuiedu.apphx.model.event.HxCallExceptionEvent;
import com.feicuiedu.apphx.model.event.HxCallStateEvent;
import com.hyphenate.chat.EMCallManager;
import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.EMNoActiveCallException;
import com.hyphenate.exceptions.EMServiceNotReadyException;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import timber.log.Timber;

public class HxCallManager implements EMCallStateChangeListener {

    private static HxCallManager sInstance;

    public static HxCallManager getInstance() {
        if (sInstance == null) {
            sInstance = new HxCallManager();
        }
        return sInstance;
    }

    private final EMCallManager emCallManager;
    private final EventBus eventBus;
    private final ExecutorService executorService;

    private HxCallManager() {
        emCallManager = EMClient.getInstance().callManager();
        emCallManager.addCallStateChangeListener(this);
        eventBus = EventBus.getDefault();
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void onCallStateChanged(CallState callState, CallError callError) {
        Timber.d("CallState is %s, CallError is %s", callState, callError);
        HxCallStateEvent event = new HxCallStateEvent(callState, callError);
        eventBus.post(event);
    }

    public void asyncVoiceCall(final String hxId) {
        Runnable runnable = new Runnable() {
            @Override public void run() {
                try {
                    emCallManager.makeVoiceCall(hxId);
                } catch (EMServiceNotReadyException e) {
                    Timber.e(e, "asyncVoiceCall");
                    eventBus.post(HxCallExceptionEvent.VOICE_CALL);
                }
            }
        };
        executorService.submit(runnable);
    }

    public void asyncAnswerCall() {
        Runnable runnable = new Runnable() {
            @Override public void run() {
                try {
                    emCallManager.answerCall();
                } catch (EMNoActiveCallException e) {
                    Timber.e(e, "asyncAnswerCall");
                    eventBus.post(HxCallExceptionEvent.ANSWER_CALL);
                }
            }
        };
        executorService.submit(runnable);
    }

    public void asyncEndCall() {
        Runnable runnable = new Runnable() {
            @Override public void run() {
                try {
                    emCallManager.endCall();
                } catch (EMNoActiveCallException e) {
                    Timber.e(e, "asyncEndCall");
                    eventBus.post(HxCallExceptionEvent.END_CALL);
                }
            }
        };
        executorService.submit(runnable);
    }

    public void asyncRejectCall() {
        Runnable runnable = new Runnable() {
            @Override public void run() {
                try {
                    emCallManager.rejectCall();
                } catch (EMNoActiveCallException e) {
                    Timber.e(e, "asyncRejectCall");
                    eventBus.post(HxCallExceptionEvent.REJECT_CALL);
                }
            }
        };
        executorService.submit(runnable);
    }

    public void setMute(boolean mute) {
        if (mute) {
            emCallManager.pauseVoiceTransfer();
        } else {
            emCallManager.resumeVideoTransfer();
        }
    }
}
