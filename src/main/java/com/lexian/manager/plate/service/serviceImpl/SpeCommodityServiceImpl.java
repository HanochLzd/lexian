package com.lexian.manager.plate.service.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lexian.utils.UrlConstant;
import org.springframework.stereotype.Service;

import com.lexian.manager.plate.bean.SpecialCommodity;
import com.lexian.manager.plate.dao.SpeCommodityDao;
import com.lexian.manager.plate.service.SpeCommodityService;
import com.lexian.utils.Constant;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

import javax.annotation.Resource;


/**
 * @author Administrator
 */
@Service
public class SpeCommodityServiceImpl implements SpeCommodityService {

    @Resource
    private SpeCommodityDao speCommodityDao;

    @Override
    public ResultHelper deleteSpeCommodity(int id) {
        speCommodityDao.deleteSpeCommodity(id);
        return new ResultHelper(Constant.CODE_SUCCESS);
    }

    @Override
    public ResultHelper getSpecialCommodities(int id, Page page) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("page", page);
        params.put("id", id);
        List<SpecialCommodity> orderssWithStore = speCommodityDao.getSpecialCommoditiesPage(params);
        for (SpecialCommodity specialCommodity : orderssWithStore) {
            specialCommodity.setPictureUrl(UrlConstant.QI_NIU_URL + specialCommodity.getPictureUrl());
        }
        page.setData(orderssWithStore);

        return new ResultHelper(Constant.CODE_SUCCESS, page);

    }

    @Override
    public ResultHelper addSpecialCommodities(String commodityNo, int specialId) {
        SpecialCommodity specialCommodity = speCommodityDao.getSpecialCommodity(commodityNo, specialId);
        if (specialCommodity != null) {
            return new ResultHelper(Constant.CODE_ENTITY_DUPLICATED);
        } else {
            speCommodityDao.addSpecialCommodities(commodityNo, specialId);
            return new ResultHelper(Constant.CODE_SUCCESS);
        }
    }

}
