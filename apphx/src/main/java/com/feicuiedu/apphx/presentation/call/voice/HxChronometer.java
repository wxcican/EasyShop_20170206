package com.feicuiedu.apphx.presentation.call.voice;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Chronometer;

public class HxChronometer extends Chronometer{

    public HxChronometer(Context context) {
        super(context);
    }

    public HxChronometer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HxChronometer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(View.VISIBLE);
    }
}
