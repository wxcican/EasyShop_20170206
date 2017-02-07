package com.fuicuiedu.xc.easyshop_20170206.commons;//package com.fuicuiedu.xc.easyshop_test_20170206.commons;
//
//import com.fuicuiedu.idedemo.easyshop.model.User;
//import com.fuicuiedu.idedemo.easyshop.network.EasyShopApi;
//import com.hyphenate.easeui.domain.EaseUser;
//import com.hyphenate.easeui.utils.EaseCommonUtils;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
///**
// * 将User转换为EaseUser(环信相关)
// */
//public class CurrentUser {
//
//    private CurrentUser() {
//    }
//
//    private static List<String> list;
//
//    public static void setList(List<String> list) {
//        CurrentUser.list = list;
//    }
//
//    public static List<String> getList() {
//        return list;
//    }
//
//    public static List<EaseUser> convertAll(List<User> users) {
//        if (users == null) {
//            //返回一个空的集合对象
//            return Collections.emptyList();
//        }
//        ArrayList<EaseUser> easeUsers = new ArrayList<>();
//        for (User user : users) {
//            easeUsers.add(convert(user));
//        }
//        return easeUsers;
//    }
//
//    public static EaseUser convert(User user) {
//        //从user取需要的信息保存到EaseUser中，完成转换
//        EaseUser easeUser = new EaseUser(user.getHx_Id());
//        easeUser.setNick(user.getNick_name());
//        easeUser.setAvatar(EasyShopApi.IMAGE_URL + user.getHead_Image());
//        EaseCommonUtils.setUserInitialLetter(easeUser);
//        return easeUser;
//    }
//}
