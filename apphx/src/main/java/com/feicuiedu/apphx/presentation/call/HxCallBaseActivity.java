package com.feicuiedu.apphx.presentation.call;


import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

public abstract class HxCallBaseActivity extends AppCompatActivity implements HxCallBaseView{


    private AudioManager audioManager;
    private Ringtone ringtone;

    private Timer timer;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringtone = RingtoneManager.getRingtone(this, ringUri);
    }


    @Override protected void onDestroy() {
        super.onDestroy();

        stopRingtone();
    }

    // start-interface: HxCallBaseView
    @Override public void playRingtone() {
        audioManager.setMode(AudioManager.MODE_RINGTONE);
        audioManager.setSpeakerphoneOn(true);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override public void run() {
                ringtone.play();
            }
        }, 0, 1000);

    }

    @Override public void stopRingtone() {
        timer.cancel();
        ringtone.stop();
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setSpeakerphoneOn(false);
    }

    @Override public void setSpeakerOn(boolean speakerOn) {
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        audioManager.setSpeakerphoneOn(speakerOn);
    } // end-interface: HxCallBaseView
}
