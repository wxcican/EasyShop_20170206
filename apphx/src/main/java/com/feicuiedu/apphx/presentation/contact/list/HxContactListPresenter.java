package com.feicuiedu.apphx.presentation.contact.list;


import android.support.annotation.NonNull;

import com.feicuiedu.apphx.basemvp.MvpPresenter;
import com.feicuiedu.apphx.model.HxContactManager;
import com.feicuiedu.apphx.model.event.HxErrorEvent;
import com.feicuiedu.apphx.model.event.HxEventType;
import com.feicuiedu.apphx.model.event.HxRefreshContactEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HxContactListPresenter extends MvpPresenter<HxContactListView> {


    @NonNull @Override protected HxContactListView getNullObject() {
        return HxContactListView.NULL;
    }

    public void getContacts() {
        HxContactManager.getInstance().retrieveContacts();
    }

    public void deleteContact(String hxId) {
        HxContactManager.getInstance().asyncDeleteContact(hxId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxRefreshContactEvent event) {

        if (event.changed) {
            getView().setContacts(event.contacts);
        }
        getView().refreshContacts();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxErrorEvent event) {

        // 判断是否是删除用户失败
        if (event.type != HxEventType.DELETE_CONTACT) return;

        getView().showDeleteContactFail(event.toString());
    }
}
