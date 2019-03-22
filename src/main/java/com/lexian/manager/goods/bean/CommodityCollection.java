package com.lexian.manager.goods.bean;

import java.util.Date;

/**
 * @author luozidong
 */
public class CommodityCollection {

    private int id;
    private String commodityNo;
    private String userId;
    private Date collectTime;
    private String storeNo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }
}
