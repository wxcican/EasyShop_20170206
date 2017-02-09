package com.fuicuiedu.xc.easyshop_20170206.network;

/**
 * 存放易淘项目的接口类
 */

public class EasyShopApi {

    //服务器地址
    static final String BASE_URL = "http://wx.feicuiedu.com:9094/yitao/";

    //图片的根路径
    public static final String IMAGE_URL = "http://wx.feicuiedu.com:9094/";

    //注册接口
    static final String REGISTER = "UserWeb?method=register";

    //登录接口
    static final String LOGIN = "UserWeb?method=login";

    //更新接口（更新昵称，更新头像）
    static final String UPDATA = "UserWeb?method=update";

    //获取商品
    static final String GETGOODS = "GoodsServlet?method=getAll";

    //获取商品详情
    static final String DETAIL = "GoodsServlet?method=view";

    //删除商品
    static final String DELETE = "GoodsServlet?method=delete";

    //上传商品
    static final String UPLOADGOODS = "GoodsServlet?method=add";

    //获取好友列表
    static final String GET_NAMES = "UserWeb?method=getNames";

    //查找好友
    static final String GET_USER = "UserWeb?method=getUsers";
}
