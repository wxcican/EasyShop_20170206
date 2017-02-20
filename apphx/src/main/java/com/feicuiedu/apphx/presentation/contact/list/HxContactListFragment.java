package com.feicuiedu.apphx.presentation.contact.list;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.feicuiedu.apphx.R;
import com.feicuiedu.apphx.presentation.chat.HxChatActivity;
import com.feicuiedu.apphx.presentation.contact.invitation.HxInvitationsActivity;
import com.feicuiedu.apphx.presentation.contact.search.HxSearchContactActivity;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;

import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

/**
 * 联系人列表(或称为通讯录、好友)页面。
 */
public class HxContactListFragment extends EaseContactListFragment implements HxContactListView {

    private HxContactListPresenter presenter;
    private String selectedHxId;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 此方法要在 onActivityCreated 之前调用才有效
        setContactListItemClickListener(new EaseContactListItemClickListener() {
            @Override public void onListItemClicked(EaseUser user) {
                Intent intent = HxChatActivity.getStartIntent(getContext(), user.getUsername());
                startActivity(intent);
            }
        });


        presenter = new HxContactListPresenter();
        presenter.onCreate();
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customUi();

        presenter.attachView(this);
        presenter.getContacts();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v == listView){
            int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
            Timber.d("onCreateContextMenu %d", position);

            // Note: 此处不能使用listView.getAdapter().getItem(position) ---
            // 在有HeaderView的情况下，ListView真正使用的适配器是HeaderViewListAdapter；
            // 因此，这里的position和我们的适配器中的position不同；
            // 另一种可行的写法是listView.getAdapter().getItem(position - listView.getHeaderViewsCount())。
            EaseUser easeUser = (EaseUser) listView.getItemAtPosition(position);

            if (easeUser == null) return; // 长按HeaderView的情况

            selectedHxId = easeUser.getUsername();
            getActivity().getMenuInflater()
                    .inflate(R.menu.fragment_hx_contact_list, menu);
        }
    }

    @Override public boolean onContextItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_delete_contact) {
            presenter.deleteContact(selectedHxId);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    // start-interface: HxContactListView
    @Override public void setContacts(List<EaseUser> contacts) {
        HashMap<String, EaseUser> contactsMap = new HashMap<>();
        contactsMap.clear();

        for (EaseUser easeUser : contacts) {
            contactsMap.put(easeUser.getUsername(), easeUser);
        }
        setContactsMap(contactsMap);
    }

    @Override public void refreshContacts() {
        super.refresh();
    }

    @Override public void showDeleteContactFail(String msg) {
        String info = getString(R.string.hx_contact_error_delete_contact, msg);
        Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
    } // end-interface: HxContactListView


    // 自定制UI
    private void customUi() {
        // 隐藏EaseUI自定义的标题栏
        hideTitleBar();

        // 联系人列表上下菜单 -- 长按弹出“删除联系人”
        registerForContextMenu(listView);

        // 设置搜索相关控件
        setSearchWidget();

        // 设置联系人列表的HeaderView
        setHeaderView();
    }

    // 设置联系人列表的HeaderView
    private void setHeaderView(){
        View headerView = LayoutInflater.from(getContext())
                .inflate(R.layout.partial_hx_contact_list_header, listView, false);

        // 添加新朋友
        View addContacts = headerView.findViewById(R.id.layout_add_contacts);
        addContacts.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent intent = new Intent(getContext(), HxSearchContactActivity.class);
                startActivity(intent);
            }
        });

        // 邀请和通知
        View notifications = headerView.findViewById(R.id.layout_notifications);
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent intent = new Intent(getContext(), HxInvitationsActivity.class);
                startActivity(intent);
            }
        });

        ListAdapter adapter = listView.getAdapter();
        listView.setAdapter(null);
        // note: 在Android4.4之前，此方法只能在ListView设置适配器之前调用。
        listView.addHeaderView(headerView);
        listView.setAdapter(adapter);
    }

    // 设置搜索相关控件
    @SuppressWarnings({"deprecation", "ConstantConditions"})
    private void setSearchWidget(){
        clearSearch.setImageResource(R.drawable.hx_btn_clear_search); // 按钮: 清除搜索内容

        Drawable searchIcon = getResources().getDrawable(R.drawable.hx_ic_search_accent);
        searchIcon.setBounds(0, 0, searchIcon.getIntrinsicWidth(), searchIcon.getIntrinsicHeight());
        query.setCompoundDrawables(searchIcon, null, null, null); // 设置查询编辑框左侧的图标
    }

}
