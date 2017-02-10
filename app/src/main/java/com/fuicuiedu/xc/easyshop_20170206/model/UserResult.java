package com.fuicuiedu.xc.easyshop_20170206.model;

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
    private String msg;

    //alt + insert......生成get和set方法

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
