package com.fuicuiedu.xc.easyshop_20170206.model;

import com.google.gson.annotations.SerializedName;

/**
 * {
 "code": 1,                 //结果码
 "msg": "succeed",            //描述信息
 "data": {                      //用户相关
     "username": "xc62",
     "name": "yt59856b15cf394e7b84a7d48447d16098",
     "uuid": "0F8EC12223174657B2E842076D54C361",
     "password": "123456"
 }
 */

public class UserResult {
    private int code;

    @SerializedName("msg")
    private String message;

    private User data;

    //alt + insert......生成get和set方法

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public User getData() {
        return data;
    }
}
