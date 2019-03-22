package com.lexian.manager.shop.service.impl;

import org.springframework.stereotype.Service;

import com.lexian.manager.shop.dao.CitysDao;
import com.lexian.manager.shop.service.CitysService;
import com.lexian.utils.Constant;
import com.lexian.web.ResultHelper;

import javax.annotation.Resource;

@Service
public class CitysServiceImpl implements CitysService {

    @Resource
    private CitysDao citysDao;

    @Override
    public ResultHelper getCities(Integer parentId) {
        // TODO Auto-generated method stub
        ResultHelper result = new ResultHelper(Constant.CODE_SUCCESS, citysDao.getCitiesByParentId(parentId));
        return result;
    }

}
