package com.feicuiedu.apphx.model.repository;

import com.hyphenate.easeui.domain.EaseUser;

import java.util.List;

/**
 * 远程用户仓库的接口定义，代表到应用服务器获取用户数据的相关操作。
 * <p/>
 * 为了测试，本模块中有一个假实现类{@link MockRemoteUsersRepo}。app模块需要通过
 * {@link com.feicuiedu.apphx.HxModuleInitializer#setRemoteUsersRepo(IRemoteUsersRepo)}
 * 方法设置真实的实现类。
 * <p/>
 * 此接口的实现类只负责从服务器获取数据，不负责将数据存储到本地。
 * <p/>
 * 网络操作是否做缓存由此接口的实现类自行决定。
 */
public interface IRemoteUsersRepo {

    /**
     * 通过用户名查询用户
     */
    List<EaseUser> queryByName(String username) throws Exception;


    /**
     * 通过环信Id查询用户信息
     */
    @SuppressWarnings("UnusedParameters")
    List<EaseUser> getUsers(List<String> ids) throws Exception;
}
