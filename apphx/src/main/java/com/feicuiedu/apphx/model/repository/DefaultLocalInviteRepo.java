package com.feicuiedu.apphx.model.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.feicuiedu.apphx.model.entity.InviteMessage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressLint("CommitPrefEdits")
public class DefaultLocalInviteRepo implements ILocalInviteRepo {


    private static final String PREF_HX_INVITE_MESSAGE_PREFIX_ = "PREF_HX_INVITE_MESSAGE_PREFIX_";
    private static DefaultLocalInviteRepo sInstance;

    public static DefaultLocalInviteRepo getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DefaultLocalInviteRepo(context.getApplicationContext());
        }
        return sInstance;
    }

    private SharedPreferences preferences;
    private String currentUserId;
    private final Context context;
    private final Gson gson;

    private DefaultLocalInviteRepo(Context context) {
        this.context = context;
        gson = new Gson();
    }

    @Override public void setCurrentUser(String hxId) {
        this.currentUserId = hxId;

        if (hxId == null) {
            this.preferences = null;
            return;
        }

        this.preferences = context.getSharedPreferences(PREF_HX_INVITE_MESSAGE_PREFIX_ + hxId, Context.MODE_PRIVATE);
    }

    @Override public void save(InviteMessage message) {
        if (currentUserId == null || !currentUserId.equals(message.getToHxId())) {
            throw new RuntimeException("Wrong current user: " + currentUserId);
        }

        preferences.edit()
                .putString(message.getFromHxId(), gson.toJson(message))
                .commit();
    }

    @NonNull @Override public List<InviteMessage> getAll() {
        if (currentUserId == null) {
            throw new RuntimeException("No current user!");
        }

        Map<String, ?> map = preferences.getAll();

        ArrayList<InviteMessage> messages = new ArrayList<>();

        for(Map.Entry<String,?> entry : map.entrySet()){
            InviteMessage inviteMessage = gson.fromJson(entry.getValue().toString(), InviteMessage.class);
            messages.add(inviteMessage);
        }
        return messages;
    }

    @Override public void delete(String fromHxId) {

        if (currentUserId == null) {
            throw new RuntimeException("No current user!");
        }

        preferences.edit()
                .remove(fromHxId)
                .commit();

    }


}
