package com.fuicuiedu.xc.easyshop_20170206.components;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.fuicuiedu.xc.easyshop_20170206.R;


/**
 * 自定义的消息弹窗,用来消息提示,不做其它用途
 */
public class AlertDialogFragment extends DialogFragment {

    private static final String KEY_TITLE = "title";
    private static final String KEY_MESSAGE = "message";

    @SuppressWarnings("unused")
    public static AlertDialogFragment newInstance(int title) {
        AlertDialogFragment fragment = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    public static AlertDialogFragment newInstance(String msg) {
        AlertDialogFragment fragment = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_TITLE, R.string.username_pwd_rule);
        args.putString(KEY_MESSAGE, msg);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt(KEY_TITLE);
        String msg = getArguments().getString(KEY_MESSAGE);
        return new AlertDialog.Builder(getActivity(), getTheme())
                .setTitle(title)
                .setMessage(msg)
                .setNeutralButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }
}
