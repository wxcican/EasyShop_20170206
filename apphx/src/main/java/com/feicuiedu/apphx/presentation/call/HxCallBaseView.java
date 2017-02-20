package com.feicuiedu.apphx.presentation.call;


import com.feicuiedu.apphx.basemvp.MvpView;

public interface HxCallBaseView extends MvpView{

    void playRingtone();

    void stopRingtone();

    void setSpeakerOn(boolean speakerOn);
}
