package com.fuicuiedu.xc.easyshop_20170206.model;

/**
 * 个人信息页面展示的实体类
 */

public class ItemShow {

    //单行布局的名称
    private String item_title;
    //单行布局的内容
    private String item_content;

    public ItemShow(String item_title, String item_content){
        this.item_title = item_title;
        this.item_content = item_content;
    }

    public String getItem_title() {
        return item_title;
    }

    public String getItem_content() {
        return item_content;
    }
}
