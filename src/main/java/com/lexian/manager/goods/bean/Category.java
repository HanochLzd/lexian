package com.lexian.manager.goods.bean;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Range;


/**
 * @author luozidong
 */
public class Category {

    private Integer id;
    @Pattern(regexp = "^[a-zA-Z]|[\u4e00-\u9fa5]{1,10}$")
    private String categoryName;
    @Range(max = 3, min = 1)
    private int type;
    private Integer parentId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
