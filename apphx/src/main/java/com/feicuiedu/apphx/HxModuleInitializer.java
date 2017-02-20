package com.feicuiedu.apphx;


import android.support.annotation.NonNull;

import com.feicuiedu.apphx.easeui.HxUserProfileProvider;
import com.feicuiedu.apphx.model.HxCallManager;
import com.feicuiedu.apphx.model.HxContactManager;
import com.feicuiedu.apphx.model.HxMessageManager;
import com.feicuiedu.apphx.model.HxUserManager;
import com.feicuiedu.apphx.model.repository.ILocalInviteRepo;
import com.feicuiedu.apphx.model.repository.ILocalUsersRepo;
import com.feicuiedu.apphx.model.repository.IRemoteUsersRepo;

/**
 * AppHx模块的初始化工具。在{@link HxBaseApplication#initHxModule(HxModuleInitializer)}中使用。
 * <p/>
 * 它负责将{@link ILocalUsersRepo}等接口的实现注入到相关的类中。
 */
public class HxModuleInitializer {

    private static HxModuleInitializer sInstance;

    public static HxModuleInitializer getInstance() {
        if (sInstance == null) {
            sInstance = new HxModuleInitializer();
        }
        return sInstance;
    }

    private HxModuleInitializer() {
    }

    private IRemoteUsersRepo remoteUsersRepo;

    private ILocalUsersRepo localUsersRepo;

    private ILocalInviteRepo localInviteMessageRepo;

    public HxModuleInitializer setRemoteUsersRepo(@NonNull IRemoteUsersRepo remoteUsersRepo) {
        this.remoteUsersRepo = remoteUsersRepo;
        return this;
    }

    public HxModuleInitializer setLocalUsersRepo(@NonNull ILocalUsersRepo localUsersRepo) {
        this.localUsersRepo = localUsersRepo;
        return this;
    }

    public HxModuleInitializer setLocalInviteRepo(@NonNull ILocalInviteRepo localInviteMessageRepo) {
        this.localInviteMessageRepo = localInviteMessageRepo;
        return this;
    }

    public void init() {

        Preconditions.checkNotNull(remoteUsersRepo, "Must set remoteUsersRepo before init!");
        Preconditions.checkNotNull(localInviteMessageRepo, "Must set localInviteMessageRepo before init!");
        Preconditions.checkNotNull(localUsersRepo, "Must set localUsersRepo before init!");

        HxUserManager.getInstance()
                .init(localUsersRepo);

        HxContactManager.getInstance()
                .initLocalUsersRepo(localUsersRepo)
                .initRemoteUsersRepo(remoteUsersRepo)
                .initLocalInviteRepo(localInviteMessageRepo);

        HxMessageManager.getInstance()
                .init(localUsersRepo);

        HxUserProfileProvider.getInstance()
                .init(localUsersRepo);

        HxCallManager.getInstance();
    }
}
