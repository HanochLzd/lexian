package com.lexian.manager.authority.bean;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @author luozidong
 */
@Data
public class Manager {

    private int id;

    @Pattern(regexp = "^([1-9][0-9]{4,19})$")
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$")
    private String password;

    private String info;

    @Null
    private Date createTime;

    @Null
    private Date updateTime;

    @Range(max = 1, min = 0)
    private Integer status;

    private List<Menu> menus;

}
