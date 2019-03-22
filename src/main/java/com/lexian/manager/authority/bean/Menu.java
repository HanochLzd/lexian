package com.lexian.manager.authority.bean;

import lombok.Data;

/**
 * 菜单
 *
 * @author luozidong
 */
@Data
public class Menu {

    private Integer id;

    private String url;

    private String name;

    private String backUrl;

    private Integer parentId;

}
