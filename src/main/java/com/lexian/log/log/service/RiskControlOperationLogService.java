package com.lexian.log.log.service;

import org.springframework.stereotype.Service;
import weli.risk.log.dao.RiskControlOperationLogDao;
import weli.risk.log.meta.RiskControlOperationLog;

import javax.annotation.Resource;

/**
 * @Author: luozidong
 * @Date: 2019/2/21
 * @Time: 18:02
 * Copyright © SSY All Rights Reserved.
 */
@Service
public class RiskControlOperationLogService {

    @Resource
    private RiskControlOperationLogDao controlOperationLogDao;

    public RiskControlOperationLog addOperationLog(RiskControlOperationLog riskControlOperationLog) {
        return controlOperationLogDao.add(riskControlOperationLog);
    }
}
