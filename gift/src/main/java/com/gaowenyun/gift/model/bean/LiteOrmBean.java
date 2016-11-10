package com.gaowenyun.gift.model.bean;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 *
 */
@Table("collections")
public class LiteOrmBean {
    // 自增长主键
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;
    private String name;
    private String description;
    private String price;
    private String imgUrl;
    private String webUrl;
    private String taobaoUrl;

    public String getTaobaoUrl() {
        return taobaoUrl;
    }

    public void setTaobaoUrl(String taobaoUrl) {
        this.taobaoUrl = taobaoUrl;
    }

    public LiteOrmBean(String name, String description, String price, String imgUrl, String webUrl, String taobaoUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.webUrl = webUrl;
        this.taobaoUrl = taobaoUrl;
    }

    public LiteOrmBean() {
    }

    public LiteOrmBean(String name, String description, String price, String imgUrl, String webUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.webUrl = webUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public LiteOrmBean(int id, String name, String description, String price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public LiteOrmBean(String name, String description, String price, String imgUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
