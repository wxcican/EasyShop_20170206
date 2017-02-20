package com.feicuiedu.apphx.presentation.contact.invitation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.feicuiedu.apphx.R;
import com.feicuiedu.apphx.model.entity.InviteMessage;

import java.util.List;

/**
 * 邀请和通知页面。
 */
public class HxInvitationsActivity extends AppCompatActivity
        implements HxInvitationsAdapter.OnHandleInvitationListener, HxInvitationsView {

    private HxInvitationsAdapter adapter;
    private HxInvitationsPresenter presenter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hx_invitations);
        presenter = new HxInvitationsPresenter();
        presenter.onCreate();
        presenter.attachView(this);
        presenter.getInvites();
    }

    @Override public void onContentChanged() {
        super.onContentChanged();

        initToolbar();
        initInvitationList();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter.onDestroy();
    }

    // start-interface: HxInvitationsAdapter.OnHandleInvitationListener
    @Override public void onAccept(@NonNull InviteMessage inviteMessage) {
        presenter.accept(inviteMessage);
    }

    @Override public void onRefuse(@NonNull InviteMessage inviteMessage) {
        presenter.refuse(inviteMessage);
    } // end-interface: HxInvitationsAdapter.OnHandleInvitationListener


    // start-interface: HxInvitationsView
    @Override public void refreshInvitations(List<InviteMessage> messages) {
        adapter.setData(messages);
    }

    @Override public void refreshInvitations() {
        adapter.notifyDataSetChanged();
    }

    @Override public void showActionFail() {
        Toast.makeText(this, R.string.hx_contact_action_fail, Toast.LENGTH_SHORT).show();
    } // end-interface: HxInvitationsView

    // 初始化Toolbar
    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // 初始化邀请信息ListView
    private void initInvitationList(){
        ListView listView = (ListView) findViewById(R.id.list_invitations);
        adapter = new HxInvitationsAdapter(this);
        //noinspection ConstantConditions
        listView.setAdapter(adapter);
    }

}
