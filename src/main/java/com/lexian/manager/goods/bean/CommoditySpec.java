package com.lexian.manager.goods.bean;

/**
 * @author luozidong
 */
public class CommoditySpec {

    private Integer id;
    private String commodityNo;
    private String specGroup;
    private String specName;
    private Integer states;

    public String getSpecGroup() {
        return specGroup;
    }

    public void setSpecGroup(String specGroup) {
        this.specGroup = specGroup;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public Integer getStates() {
        return states;
    }

    public void setStates(Integer states) {
        this.states = states;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
    }
}
