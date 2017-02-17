package com.fuicuiedu.xc.easyshop_20170206.model;

/**
 * 商品上传时的实体类
 */

public class GoodsUpLoad {

    /*商品名称*/
    private String name;
    /*商品价格*/
    private String price;
    /*商品描述*/
    private String description;
    /*商品类型*/
    private String type;
    /*商品发布人*/
    private String master;

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDescribe(String describe) {
        this.description = describe;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMaster(String master) {
        this.master = master;
    }
}
