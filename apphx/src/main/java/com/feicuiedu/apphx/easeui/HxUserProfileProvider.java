package com.feicuiedu.apphx.easeui;


import com.feicuiedu.apphx.model.repository.ILocalUsersRepo;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;

/**
 * 参考<a href="http://docs.easemob.com/im/200androidclientintegration/135easeuiuseguide">EaseUI 使用指南</a>中的：
 * 设置用户信息提供者。
 * <p/>
 * 注意此处的Provider不是ContentProvider，只是对EaseUI进行配置的一个接口。
 */
public class HxUserProfileProvider implements EaseUI.EaseUserProfileProvider {

    private static HxUserProfileProvider sInstance;

    public static HxUserProfileProvider getInstance() {
        if (sInstance == null) {
            sInstance = new HxUserProfileProvider();
        }

        return sInstance;
    }

    private ILocalUsersRepo localUsersRepo;

    private HxUserProfileProvider() {
    }

    public void init(ILocalUsersRepo localUsersRepo) {
        this.localUsersRepo = localUsersRepo;
    }

    @Override public EaseUser getUser(String username) {
        if (localUsersRepo == null) throw new RuntimeException("Not init!");

        return localUsersRepo.getUser(username);
    }
}
