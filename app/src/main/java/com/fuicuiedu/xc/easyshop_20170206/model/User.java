package com.fuicuiedu.xc.easyshop_20170206.model;

import com.google.gson.annotations.SerializedName;

/**
 * "username": "xc62",          //用户名
 "name": "yt59856b15cf394e7b84a7d48447d16098",          //环信id
 "uuid": "0F8EC12223174657B2E842076D54C361",            //用户表主键
 "password": "123456"          //密码
 */

public class User {

    @SerializedName("username")
    private String name;
    @SerializedName("name")
    private String hx_Id;
    @SerializedName("uuid")
    private String table_Id;
    private String password;

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
}
