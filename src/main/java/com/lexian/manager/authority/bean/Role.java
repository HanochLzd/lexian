package com.lexian.manager.authority.bean;

import java.util.Date;

import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

/**
 * @author luozidong
 */
public class Role {

    private int id;
    @Pattern(regexp = "^[A-Za-z]{5,20}$")
    private String name;
    @Pattern(regexp = "^[0-9a-zA-Z\u4e00-\u9fa5]*${1,150}")
    private String description;
    @Null
    private Date createTime;
    @Null
    private Date updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
