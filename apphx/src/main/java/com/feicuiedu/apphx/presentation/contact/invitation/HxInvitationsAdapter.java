package com.feicuiedu.apphx.presentation.contact.invitation;


import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.feicuiedu.apphx.Preconditions;
import com.feicuiedu.apphx.R;
import com.feicuiedu.apphx.model.entity.InviteMessage;
import com.hyphenate.easeui.utils.EaseUserUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 邀请信息列表适配器。
 * <p/>
 * 对于单条邀请信息，有同意/拒绝按钮，这两个按钮的点击事件不由adapter自身处理，
 * 而是通过{@link OnHandleInvitationListener}监听器传递给实现此监听接口的界面来处理。
 */
class HxInvitationsAdapter extends BaseAdapter {

    private final List<InviteMessage> messages;
    private final OnHandleInvitationListener listener;

    public HxInvitationsAdapter(@NonNull OnHandleInvitationListener listener) {
        this.messages = new ArrayList<>();
        this.listener = listener;
    }

    @Override public int getCount() {
        return messages.size();
    }

    @Override public Object getItem(int position) {
        return messages.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {

        // 如果有convertView，则重用convertView，否则创建一个新视图
        View itemView = (convertView != null) ?
                convertView : createItemView(parent);

        // 根据position更新视图上的数据
        updateItemView(position, itemView);

        return itemView;
    }

    public void setData(List<InviteMessage> data) {
        messages.clear();
        messages.addAll(data);
        notifyDataSetChanged();
    }

    private View createItemView(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_invitation, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        itemView.setTag(viewHolder);

        return itemView;
    }

    private void updateItemView(int position, View itemView) {
        ViewHolder viewHolder = (ViewHolder) itemView.getTag();
        InviteMessage inviteMessage = messages.get(position);
        viewHolder.bind(inviteMessage);
    }

    public interface OnHandleInvitationListener {
        // 当点击“同意”时
        void onAccept(@NonNull InviteMessage inviteMessage);

        // 当点击“拒绝”时
        void onRefuse(@NonNull InviteMessage inviteMessage);
    }

    private class ViewHolder implements View.OnClickListener {

        final ImageView ivAvatar; // 用户头像
        final Button btnAgree; // 同意按钮
        final Button btnRefuse; // 拒绝按钮
        final TextView tvUsername; // 用户名
        final TextView tvStatus; // 消息状态(“已同意”，“已拒绝”)
        final TextView tvInfo; // 消息内容，为了简化设为固定值

        private InviteMessage inviteMessage;

        ViewHolder(View itemView) {
            ivAvatar = (ImageView) itemView.findViewById(R.id.image_avatar);
            btnAgree = (Button) itemView.findViewById(R.id.button_agree);
            btnRefuse = (Button) itemView.findViewById(R.id.button_refuse);
            tvUsername = (TextView) itemView.findViewById(R.id.text_username);
            tvStatus = (TextView) itemView.findViewById(R.id.text_status);
            tvInfo = (TextView) itemView.findViewById(R.id.text_info);

            btnAgree.setOnClickListener(this);
            btnRefuse.setOnClickListener(this);
        }

        public void bind(InviteMessage inviteMessage) {
            this.inviteMessage = inviteMessage;
            updateView();
        }

        @Override public void onClick(View v) {
            Preconditions.checkNotNull(inviteMessage);

            if (v.getId() == R.id.button_agree) {
                listener.onAccept(inviteMessage);
            } else if (v.getId() == R.id.button_refuse) {
                listener.onRefuse(inviteMessage);
            }
        }

        private void updateView() {
            switch (inviteMessage.getStatus()) {
                case RAW:
                    btnAgree.setVisibility(View.VISIBLE);
                    btnRefuse.setVisibility(View.VISIBLE);
                    tvStatus.setVisibility(View.GONE);
                    tvInfo.setText(R.string.hx_contact_invitation_reason);
                    break;
                case ACCEPTED:
                    btnAgree.setVisibility(View.GONE);
                    btnRefuse.setVisibility(View.GONE);
                    tvStatus.setVisibility(View.VISIBLE);
                    tvStatus.setText(R.string.hx_contact_invitation_agreed);
                    tvInfo.setText(R.string.hx_contact_invitation_reason);
                    break;
                case REFUSED:
                    btnAgree.setVisibility(View.GONE);
                    btnRefuse.setVisibility(View.GONE);
                    tvStatus.setVisibility(View.VISIBLE);
                    tvStatus.setText(R.string.hx_contact_invitation_refused);
                    tvInfo.setText(R.string.hx_contact_invitation_reason);
                    break;
                case REMOTE_ACCEPTED:
                    btnAgree.setVisibility(View.GONE);
                    btnRefuse.setVisibility(View.GONE);
                    tvStatus.setVisibility(View.GONE);
                    tvInfo.setText(R.string.hx_contact_invitation_accept);
                    break;
                default:
                    throw new RuntimeException("Wrong branch!");
            }

            // 使用EaseUI工具类，设置用户名和头像
            EaseUserUtils.setUserNick(inviteMessage.getFromHxId(), tvUsername);
            EaseUserUtils.setUserAvatar(ivAvatar.getContext(), inviteMessage.getFromHxId(), ivAvatar);
        }
    }
}
