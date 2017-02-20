package com.feicuiedu.apphx.model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.hyphenate.easeui.domain.EaseUser;

import java.util.List;

/**
 * 本地用户信息仓库的接口定义。
 * <p/>
 * 为了简化，本模块中有一个使用{@link android.content.SharedPreferences}的简单实现类{@link DefaultLocalUsersRepo}。
 * <p/>
 * 应用可以用数据库方式重新实现此接口，然后通过{@link com.feicuiedu.apphx.HxModuleInitializer#setLocalUsersRepo(ILocalUsersRepo)}
 * 方法设置默认的仓库实现。
 * <p/>
 * 本地数据存储是否做内存缓存由此接口的实现类自行决定。
 */
public interface ILocalUsersRepo {

    /**
     * @param userList 要保存的用户列表
     */
    void saveAll(@NonNull List<EaseUser> userList);

    void save(@NonNull EaseUser easeUser);


    /**
     * @param hxId 环信id
     * @return EaseUI模块定义的用户实体
     */
    @Nullable EaseUser getUser(String hxId);
}
