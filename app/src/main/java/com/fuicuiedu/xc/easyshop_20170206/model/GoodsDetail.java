package com.fuicuiedu.xc.easyshop_20170206.model;

import java.io.Serializable;
import java.util.List;

/**
 * 商品展示详情类
 */
@SuppressWarnings("unused")
public class GoodsDetail implements Serializable {

    /*名称*/
    private String name;
    /*类型*/
    private String type;
    /*价格*/
    private String price;
    /*商品描述*/
    private String description;
    /*发布者*/
    private String master;
    /*商品图片uri*/
    private List<ImageUri> pages;


    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getMaster() {
        return master;
    }

    public List<ImageUri> getPages() {
        return pages;
    }

    public class ImageUri {
        private String uri;

        public String getUri() {
            return uri;
        }
    }

}
