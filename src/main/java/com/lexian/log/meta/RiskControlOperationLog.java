//package com.lexian.log.log.meta;
//
//import lombok.Builder;
//import lombok.Data;
//import lombok.ToString;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.index.CompoundIndex;
//import org.springframework.data.mongodb.core.index.CompoundIndexes;
//import org.springframework.data.mongodb.core.mapping.Document;
//
///**
// * @Author: luozidong
// * @Date: 2019/2/21
// * @Time: 17:46
// * Copyright © SSY All Rights Reserved.
// */
//@Data
//@Builder
//@ToString
//@Document(collection = "risk_control_operation_log")
//@CompoundIndexes({
//        @CompoundIndex(name = "account_index", def = "{'account':1}"),
//        @CompoundIndex(name = "date_index", def = "{'date':1}")})
//public class RiskControlOperationLog {
//
//    @Id
//    private String id;
//
//    /**
//     * 登录用户账户
//     */
//    private String account;
//
//    /**
//     * 登录用户名
//     */
//    private String userName;
//
//    /**
//     * 日志类型（PUT、POST、DELETE）
//     */
//    private String type;
//
//    /**
//     * 操作数据
//     */
//    private String data;
//
//    private String date;
//
//    private Long createTime;
//}
