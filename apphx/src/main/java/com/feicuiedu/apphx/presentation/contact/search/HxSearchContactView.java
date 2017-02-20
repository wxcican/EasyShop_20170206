package com.feicuiedu.apphx.presentation.contact.search;


import com.feicuiedu.apphx.basemvp.MvpView;
import com.hyphenate.easeui.domain.EaseUser;

import java.util.List;

public interface HxSearchContactView extends MvpView{

    void startLoading();

    void stopLoading();

    /**
     * @param contacts 环信Id的集合
     */
    void showContacts(List<EaseUser> contacts);

    void showSearchError(String error);

    void showSendInviteResult(boolean success);

    void showAlreadyIsFriend();

    HxSearchContactView NULL = new HxSearchContactView() {
        @Override public void startLoading() {
        }

        @Override public void stopLoading() {
        }

        @Override public void showContacts(List<EaseUser> contacts) {
        }

        @Override public void showSearchError(String error) {
        }

        @Override public void showSendInviteResult(boolean success) {
        }

        @Override public void showAlreadyIsFriend() {
        }
    };
}
