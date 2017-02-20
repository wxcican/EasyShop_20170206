package com.feicuiedu.apphx.model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.feicuiedu.apphx.model.entity.InviteMessage;

import java.util.List;

/**
 * 本地邀请信息仓库的接口定义。
 * <p/>
 * 为了简化，在本模块中有一个使用{@link android.content.SharedPreferences}的简单实现类{@link DefaultLocalInviteRepo}。
 * <p/>
 * 应用可以用数据库方式重新实现此接口，然后通过{@link com.feicuiedu.apphx.HxModuleInitializer#setLocalInviteRepo(ILocalInviteRepo)}
 * 方法设置默认的仓库实现。
 * <p/>
 * 本地数据存储是否做内存缓存由此接口的实现类自行决定。
 */
public interface ILocalInviteRepo {

    /**
     * @param hxId 当前用户的环信id，或用户登出，则设为null。
     */
    void setCurrentUser(@Nullable String hxId);

    void save(InviteMessage message);

    /**
     * @return 当前用户的所有邀请信息。
     */
    @NonNull List<InviteMessage> getAll();

    /**
     * 删除来自另一个用户的好友邀请。
     *
     * @param fromHxId 对方的环信id
     */
    void delete(String fromHxId);

}
