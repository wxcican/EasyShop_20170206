package com.feicuiedu.apphx.model.repository;


import com.hyphenate.easeui.domain.EaseUser;

import java.util.ArrayList;
import java.util.List;

public class MockRemoteUsersRepo implements IRemoteUsersRepo{


    @Override public List<EaseUser> queryByName(String username) throws Exception {

        Thread.sleep(3000);

        if ("error".equals(username)) {
            throw new Exception("Illegal query parameters!");
        }

        ArrayList<EaseUser> easeUsers = new ArrayList<>();
        easeUsers.add(new EaseUser("ycj"));
        easeUsers.add(new EaseUser("yc"));
        easeUsers.add(new EaseUser("rx"));
        return easeUsers;
    }

    @Override public List<EaseUser> getUsers(List<String> ids) throws Exception {
        return null;
    }
}
