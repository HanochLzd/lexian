/**
*  Copyright 2017  Chinasofti , Inc. All rights reserved.
*/
package com.lexian.manager.shop.bean;

/**
 * 
 * <p>Title: 乐鲜生活</p>
 * <p>Description: 乐鲜生活购物系统</p>
 * <p>Copyright: Copyright (c) 200x</p>
 * <p>Company: 中软国际</p>
 * @author 陈浩
 * @version 1.0
 */
public class CommodityStore {

	private Integer id;
	private String commodityNo;
	private String storeNo;
	private Double commodotyPrice;
	private Double realPrice;
	private Integer commodityAmont;
	private Integer commodityLockAmont;
	private Integer type;
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getStoreNo() {
		return storeNo;
	}
	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}
	public Double getCommodotyPrice() {
		return commodotyPrice;
	}
	public void setCommodotyPrice(Double commodotyPrice) {
		this.commodotyPrice = commodotyPrice;
	}
	public Double getRealPrice() {
		return realPrice;
	}
	public void setRealPrice(Double realPrice) {
		this.realPrice = realPrice;
	}
	public Integer getCommodityAmont() {
		return commodityAmont;
	}
	public void setCommodityAmont(Integer commodityAmont) {
		this.commodityAmont = commodityAmont;
	}
	public Integer getCommodityLockAmont() {
		return commodityLockAmont;
	}
	public void setCommodityLockAmont(Integer commodityLockAmont) {
		this.commodityLockAmont = commodityLockAmont;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	
}
