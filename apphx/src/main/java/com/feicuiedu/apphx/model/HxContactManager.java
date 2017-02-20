package com.feicuiedu.apphx.model;

import com.feicuiedu.apphx.Preconditions;
import com.feicuiedu.apphx.model.entity.InviteMessage;
import com.feicuiedu.apphx.model.event.HxErrorEvent;
import com.feicuiedu.apphx.model.event.HxEventType;
import com.feicuiedu.apphx.model.event.HxRefreshContactEvent;
import com.feicuiedu.apphx.model.event.HxRefreshInviteEvent;
import com.feicuiedu.apphx.model.event.HxSearchContactEvent;
import com.feicuiedu.apphx.model.event.HxSimpleEvent;
import com.feicuiedu.apphx.model.repository.ILocalInviteRepo;
import com.feicuiedu.apphx.model.repository.ILocalUsersRepo;
import com.feicuiedu.apphx.model.repository.IRemoteUsersRepo;
import com.google.gson.Gson;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMContactManager;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import timber.log.Timber;

/**
 * 环信联系人管理类
 */
public class HxContactManager implements EMConnectionListener, EMContactListener {

    private static HxContactManager sInstance;

    public static HxContactManager getInstance() {
        if (sInstance == null) {
            sInstance = new HxContactManager();
        }
        return sInstance;
    }

    private final ExecutorService executorService;
    private final EMContactManager emContactManager;
    private final EventBus eventBus;
    private final Gson gson;

    private ILocalUsersRepo localUsersRepo;
    private IRemoteUsersRepo remoteUsersRepo;
    private ILocalInviteRepo localInviteRepo;

    private List<String> contacts;
    private String currentUserId;

    private HxContactManager() {
        executorService = Executors.newSingleThreadExecutor();

        EMClient emClient = EMClient.getInstance();
        emClient.addConnectionListener(this);

        emContactManager = emClient.contactManager();
        emContactManager.setContactListener(this);

        eventBus = EventBus.getDefault();
        gson = new Gson();
    }

    // start-interface: EMConnectionListener
    @Override public void onConnected() {
        if (contacts == null) {
            asyncGetContactsFromServer();
        }
    }

    /* no-op */
    @Override public void onDisconnected(int i) {
    } // end-interface: EMConnectionListener


    // start-interface: EMContactListener

    /**
     * 添加联系人。
     */
    @Override public void onContactAdded(String hxId) {
        Timber.d("onContactAdded %s", hxId);
        if (contacts == null) {
            asyncGetContactsFromServer();
        } else {
            contacts.add(hxId);
            notifyContactsRefresh();
        }
    }

    /**
     * 删除联系人。
     */
    @Override public void onContactDeleted(String hxId) {
        Timber.d("onContactDeleted %s", hxId);
        if (contacts == null) {
            asyncGetContactsFromServer();
        } else {
            contacts.remove(hxId);
            notifyContactsRefresh();
        }

        localInviteRepo.delete(hxId);
        notifyInviteRefresh();
    }

    /**
     * 收到好友邀请
     * <p/>
     * A 向 B发送邀请时，B的这个方法会被调用
     *
     * @param hxId   环信ID
     * @param reason 理由
     */
    @Override public void onContactInvited(String hxId, String reason) {
        Timber.d("onContactInvited %s, reason: %s", hxId, reason);

        EaseUser easeUser = gson.fromJson(reason, EaseUser.class);
        localUsersRepo.save(easeUser);

        InviteMessage inviteMessage = new InviteMessage(hxId, currentUserId, InviteMessage.Status.RAW);
        localInviteRepo.save(inviteMessage);
        notifyInviteRefresh();
    }

    /**
     * 好友请求被同意
     * <p/>
     * B 同意 A的好友邀请时，A的这个方法会被调用
     */
    @Override public void onContactAgreed(String hxId) {
        Timber.d("onContactAgreed %s", hxId);

        InviteMessage inviteMessage = new InviteMessage(hxId, currentUserId, InviteMessage.Status.REMOTE_ACCEPTED);
        localInviteRepo.save(inviteMessage);
        notifyInviteRefresh();
    }

    /**
     * 好友请求被拒绝
     */
    /* no-op */
    @Override public void onContactRefused(String hxId) {
        Timber.d("onContactRefused %s", hxId);
    } // end-interface: EMContactListener


    public boolean isFriend(String hxId) {
        return contacts != null && contacts.contains(hxId);
    }


    public void retrieveContacts() {
        if (contacts != null) {
            notifyContactsRefresh();
        } else {
            asyncGetContactsFromServer();
        }
    }


    /**
     * 删除联系人，如果删除成功，会自动触发{@link #onContactDeleted(String)}
     * <p/>
     * 注意：A把B删除了，B客户端的{@link #onContactDeleted(String)}也会触发。
     *
     * @param hxId 对方的环信Id
     */
    public void asyncDeleteContact(final String hxId) {
        Runnable runnable = new Runnable() {
            @Override public void run() {
                try {
                    emContactManager.deleteContact(hxId);
                } catch (HyphenateException e) {
                    Timber.e(e, "asyncDeleteContact");
                    // 删除失败
                    eventBus.post(new HxErrorEvent(HxEventType.DELETE_CONTACT, e));
                }
            }
        };

        executorService.submit(runnable);
    }

    /**
     * 发送好友邀请
     */
    public void asyncSendInvite(final String hxId) {
        final EaseUser easeUser = localUsersRepo.getUser(currentUserId);

        Runnable runnable = new Runnable() {
            @Override public void run() {
                try {
                    emContactManager.addContact(hxId, gson.toJson(easeUser));
                    eventBus.post(new HxSimpleEvent(HxEventType.SEND_INVITE));
                } catch (HyphenateException e) {
                    Timber.e(e, "asyncSendInvite");
                    eventBus.post(new HxErrorEvent(HxEventType.SEND_INVITE, e));
                }
            }
        };

        executorService.submit(runnable);
    }

    /**
     * 接受好友邀请。
     * <p/>
     * 操作成功发送{@link HxSimpleEvent}事件，类型为{@link HxEventType#ACCEPT_INVITE}。
     * <p/>
     * 操作失败发送{@link HxErrorEvent}事件，类型为{@link HxEventType#ACCEPT_INVITE}。
     */
    public void asyncAcceptInvite(final InviteMessage invite) {

        Preconditions.checkArgument(currentUserId.equals(invite.getToHxId()));

        Runnable runnable = new Runnable() {
            @Override public void run() {
                try {
                    // Note: 此方法若成功，环信会自动触发双方的onContactAdded方法
                    emContactManager.acceptInvitation(invite.getFromHxId());

                    // Note: 此处直接修改了对象的值，会直接影响UI界面的数据
                    invite.setStatus(InviteMessage.Status.ACCEPTED);
                    localInviteRepo.save(invite);

                    eventBus.post(new HxSimpleEvent(HxEventType.ACCEPT_INVITE));
                } catch (HyphenateException e) {
                    Timber.e(e, "asyncAcceptInvite");
                    eventBus.post(new HxErrorEvent(HxEventType.ACCEPT_INVITE, e));
                }
            }
        };

        executorService.submit(runnable);
    }

    /**
     * 拒绝好友邀请
     */
    public void asyncRefuseInvite(final InviteMessage invite) {

        Preconditions.checkArgument(currentUserId.equals(invite.getToHxId()));

        Runnable runnable = new Runnable() {
            @Override public void run() {
                try {
                    emContactManager.declineInvitation(invite.getFromHxId());

                    // Note: 此处直接修改了对象的值，会直接影响UI界面的数据
                    invite.setStatus(InviteMessage.Status.REFUSED);
                    localInviteRepo.save(invite);

                    eventBus.post(new HxSimpleEvent(HxEventType.REFUSE_INVITE));
                } catch (HyphenateException e) {
                    Timber.e(e, "declineInvite");
                    eventBus.post(new HxErrorEvent(HxEventType.REFUSE_INVITE, e));
                }
            }
        };

        executorService.submit(runnable);
    }

    public void getInvites() {
        executorService.submit(new Runnable() {
            @Override public void run() {
                notifyInviteRefresh();
            }
        });
    }


    /**
     * 搜索用户，搜索结果使用{@link HxSearchContactEvent}事件传递。
     *
     * @param username 搜索参数，应用中是按用户名搜索
     */
    public void asyncSearchContacts(final String username) {
        Runnable runnable = new Runnable() {
            @Override public void run() {
                try {
                    // 从应用服务器查询用户列表
                    List<EaseUser> users = remoteUsersRepo.queryByName(username);
                    // 将查询到的接口存储到本地数据仓库中
                    localUsersRepo.saveAll(users);

                    // 将结果发送给Presenter
                    eventBus.post(new HxSearchContactEvent(users));
                } catch (Exception e) {
                    Timber.e(e, "asyncSearchContacts");
                    eventBus.post(new HxSearchContactEvent(e.getMessage()));
                }
            }
        };
        executorService.submit(runnable);
    }

    @SuppressWarnings("UnusedReturnValue")
    public HxContactManager initRemoteUsersRepo(IRemoteUsersRepo remoteUsersRepo) {
        this.remoteUsersRepo = remoteUsersRepo;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public HxContactManager initLocalUsersRepo(ILocalUsersRepo localUsersRepo) {
        this.localUsersRepo = localUsersRepo;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public HxContactManager initLocalInviteRepo(ILocalInviteRepo localInviteRepo) {
        this.localInviteRepo = localInviteRepo;
        return this;
    }

    public void setCurrentUser(String hxId) {
        this.currentUserId = hxId;
        localInviteRepo.setCurrentUser(hxId);
    }

    public void reset() {
        contacts = null;
        currentUserId = null;
        localInviteRepo.setCurrentUser(null);
    }

    List<String> getContacts() {
        return contacts;
    }

    public boolean isContact(String hxId) {

        return contacts != null && contacts.contains(hxId);

    }


    public EaseUser getUser(String hxId){
        return localUsersRepo.getUser(hxId);
    }

    private void asyncGetContactsFromServer() {
        Runnable runnable = new Runnable() {
            @Override public void run() {
                try {
                    contacts = emContactManager.getAllContactsFromServer();
                    notifyContactsRefresh();
                } catch (HyphenateException e) {
                    Timber.e(e, "asyncGetContactsFromServer");
                }
            }
        };

        executorService.submit(runnable);
    }

    private void notifyContactsRefresh() {

        Preconditions.checkNotNull(contacts);

        Runnable runnable = new Runnable() {
            @Override public void run() {

                List<EaseUser> currentContacts = new ArrayList<>();


                List<String> noInfoContacts = new ArrayList<>();

                // 分别找到本地有数据和没数据的联系人
                for (String hxId : contacts) {

                    EaseUser easeUser = localUsersRepo.getUser(hxId);

                    if (easeUser != null) {
                        currentContacts.add(easeUser);
                    } else {
                        noInfoContacts.add(hxId);
                    }
                }

                // 发送刷新联系人事件
                eventBus.post(new HxRefreshContactEvent(currentContacts));

                if (noInfoContacts.size() == 0) return;

                try {

                    // 从远程用户仓库获取联系人信息
                    List<EaseUser> easeUsers = remoteUsersRepo.getUsers(noInfoContacts);
                    // 将信息存储到本地用户仓库
                    localUsersRepo.saveAll(easeUsers);

                    currentContacts.addAll(easeUsers);

                    // 再次发送刷新联系人事件
                    eventBus.post(new HxRefreshContactEvent(currentContacts));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        executorService.submit(runnable);
    }

    private void notifyInviteRefresh() {
        List<InviteMessage> messages = localInviteRepo.getAll();
        eventBus.post(new HxRefreshInviteEvent(messages));
    }
}
