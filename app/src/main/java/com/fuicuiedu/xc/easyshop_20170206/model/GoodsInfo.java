package com.fuicuiedu.xc.easyshop_20170206.model;

/**
 * 商品的实体类
 *
 * "price": "66",        //商品价格
 "name": "单车",      //商品名称
 "description": "......",  //商品描述
 "page": "/images/D3228118230A43C0B77/5606FF8A48F1FC4907D/F99E38F09A.JPEG", //商品的第一张图片
 "type": "other",      //商品类型
 "uuid": "5606FF8EF60146A48F1FCDC34144907D",  //商品表中主键
 "master": "android"  //发布者
 */

public class GoodsInfo {

    private String id;
    /*商品价格*/
    private String price;
    /*商品图片URL*/
    private String page;
    /*商品名称*/
    private String name;
    /*商品在商品表中的主键*/
    private String uuid;
    /*商品类型*/
    private String type;
    /*商品的发布者*/
    private String master;

    public void setId(String id) {
        this.id = id;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public String getPage() {
        return page;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    public String getType() {
        return type;
    }

    public String getMaster() {
        return master;
    }
}
