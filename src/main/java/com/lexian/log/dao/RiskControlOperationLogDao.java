//package com.lexian.log.log.dao;
//
//import org.springframework.data.domain.Sort;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.stereotype.Repository;
//import suishen.framework.dao.mongo.AbstractMongoBaseDao;
//import weli.risk.log.meta.RiskControlOperationLog;
//
//import java.util.List;
//
///**
// * @Author: luozidong
// * @Date: 2019/2/21
// * @Time: 18:02
// * Copyright © SSY All Rights Reserved.
// */
//@Repository
//public class RiskControlOperationLogDao extends AbstractMongoBaseDao<RiskControlOperationLog> {
//
//    public RiskControlOperationLog getLogByid(Long id){
//        Criteria criteria =Criteria.where("id").is(id);
//        return findOneByQuery(Query.query(criteria));
//    }
//
//    public List<RiskControlOperationLog> getLogsByAccount(String account,String startDay ,String endDay,Integer limit,Integer offset){
//        Criteria criteria = Criteria.where("account").is(account);
//        criteria.and("date").gte(startDay).lte(endDay);
//        return findByQuery(Query.query(criteria).with(new Sort(Sort.Direction.DESC,"date")),limit,offset);
//    }
//
//    public List<RiskControlOperationLog> getLogsByUsername(String userName){
//        return null;
//    }
//}
