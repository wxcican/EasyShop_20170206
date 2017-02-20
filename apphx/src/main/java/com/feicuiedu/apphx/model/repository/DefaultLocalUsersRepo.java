package com.feicuiedu.apphx.model.repository;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.hyphenate.easeui.domain.EaseUser;

import java.util.List;

public class DefaultLocalUsersRepo implements ILocalUsersRepo {


    private static final String PREF_HX_USERS_FILE_NAME = "PREF_HX_USERS_FILE_NAME";
    private static DefaultLocalUsersRepo sInstance;

    public static DefaultLocalUsersRepo getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DefaultLocalUsersRepo(context.getApplicationContext());
        }
        return sInstance;
    }

    private final SharedPreferences preferences;
    private final Gson gson;
    private DefaultLocalUsersRepo(Context context) {
        preferences = context.getSharedPreferences(PREF_HX_USERS_FILE_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }


    @Override public void saveAll(@NonNull List<EaseUser> userList) {

        SharedPreferences.Editor editor = preferences.edit();

        for (EaseUser user : userList) {
            editor.putString(user.getUsername(), gson.toJson(user));
        }
        editor.apply();
    }

    @Override public void save(@NonNull EaseUser easeUser) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(easeUser.getUsername(), gson.toJson(easeUser));
        editor.apply();
    }

    @Nullable @Override public EaseUser getUser(String hxId) {

        String userStr = preferences.getString(hxId, null);

        if (userStr == null) return null;

        return gson.fromJson(userStr, EaseUser.class);
    }
}
