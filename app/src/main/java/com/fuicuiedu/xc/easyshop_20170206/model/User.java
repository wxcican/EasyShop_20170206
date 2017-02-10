package com.fuicuiedu.xc.easyshop_20170206.model;

import com.google.gson.annotations.SerializedName;

/**
 * "username": "xc62",          //用户名
 "name": "yt59856b15cf394e7b84a7d48447d16098",          //环信id
 "uuid": "0F8EC12223174657B2E842076D54C361",            //用户表主键
 "password": "123456"          //密码

 "other": "/images/35C69D35E4164D19B4278DC45FDCAF45/2D505F81BB.jpg",    //用户头像
 "nickname": "666",     //用户昵称
 */

public class User {

    @SerializedName("username")
    private String name;
    @SerializedName("name")
    private String hx_Id;
    @SerializedName("uuid")
    private String table_Id;
    private String password;

    @SerializedName("other")
    private String head_Image;
    @SerializedName("nickname")
    private String nick_name;

    public String getName() {
        return name;
    }

    public String getHx_Id() {
        return hx_Id;
    }

    public String getTable_Id() {
        return table_Id;
    }

    public String getPassword() {
        return password;
    }

    public String getHead_Image() {
        return head_Image;
    }

    public String getNick_name() {
        return nick_name;
    }
}
