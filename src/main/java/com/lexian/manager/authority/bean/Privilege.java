package com.lexian.manager.authority.bean;


import lombok.Data;

/**
 * 权限
 *
 * @author luozidong
 */
@Data
public class Privilege {

    /**
     * 自增长
     */
    private int id;

    /**
     * 该权限所对应的服务端URL地址
     * 例如一个html页面地址或者一个服务借口（.do）地址
     */
    private String url;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限信息描述
     */
    private String description;

}
