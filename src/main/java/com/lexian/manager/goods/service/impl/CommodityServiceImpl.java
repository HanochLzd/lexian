package com.lexian.manager.goods.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lexian.cache.annotation.Cacheable;
import com.lexian.utils.UrlConstant;
import org.springframework.stereotype.Service;

import com.lexian.manager.goods.bean.Commodity;
import com.lexian.manager.goods.bean.CommoditySpec;
import com.lexian.manager.goods.dao.CommodityDao;
import com.lexian.manager.goods.service.CommodityService;
import com.lexian.utils.Constant;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

import javax.annotation.Resource;


/**
 * @author Administrator
 */
@Service
public class CommodityServiceImpl {

    @Resource
    private CommodityDao commodityDao;


    @Cacheable
    public ResultHelper getCommodities(Page page) {

        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        List<Commodity> orderssWithStore = commodityDao.getCommoditiesPage(params);
        for (Commodity commodity : orderssWithStore) {
            commodity.setPictureUrl(UrlConstant.QI_NIU_URL + commodity.getPictureUrl());
        }
        page.setData(orderssWithStore);

        return new ResultHelper(Constant.CODE_SUCCESS, page);
    }


    public ResultHelper getCommodityByCategoryId(int categoryId) {

        List<Commodity> list = commodityDao.getCommodityByCategoryId(categoryId);
        return new ResultHelper(Constant.CODE_SUCCESS, list);

    }


    public ResultHelper getCommodityByCommodityNo(String commodityNo) {
        Commodity commodity = commodityDao.getCommodityByCommodityNo(commodityNo);
        if (commodity != null) {
            return new ResultHelper(Constant.CODE_SUCCESS, commodity);
        } else {
            return new ResultHelper(Constant.CODE_ENTITY_NOT_FOUND, commodity);
        }
    }


    public ResultHelper updateCommodity(Commodity commodity) {
        Date time = new Date();
        commodity.setUpdateTime(time);
        if (commodity.getPictureUrl() != null) {
            commodity.setPictureUrl(commodity.getPictureUrl().replace(UrlConstant.QI_NIU_URL, ""));
        }
        commodityDao.deleteCommodityPicture(commodity.getCommodityNo());
        commodityDao.deleteCommoditySpec(commodity.getCommodityNo());

        //List<CommodityPicuture> newComPic=new LinkedList<>();
        //List<CommodityPicuture> newComPic2=new LinkedList<>();

        List<String> listCommodityPicture = commodity.getCommodityPicuture();
        if (listCommodityPicture.size() != 0) {
            for (String string : listCommodityPicture) {
                commodityDao.addCommodityPicture(commodity.getCommodityNo(),
                        string.replace(UrlConstant.QI_NIU_URL, ""));
            }
        } else {
            commodityDao.addCommodityPicture(commodity.getCommodityNo(), null);
        }


        List<CommoditySpec> listCommoditySpec = commodity.getCommodtySpecs();
        if (listCommoditySpec.size() != 0) {
            for (CommoditySpec commoditySpec : listCommoditySpec) {
                commoditySpec.setCommodityNo(commodity.getCommodityNo());
                commodityDao.addCommoditySpec(commoditySpec);
            }
        } else {
            CommoditySpec commoditySpec = new CommoditySpec();
            commoditySpec.setStates(1);
            commoditySpec.setCommodityNo(commodity.getCommodityNo());
            commodityDao.addCommoditySpec(commoditySpec);
        }
        commodityDao.updateCommodity(commodity);
        return new ResultHelper(Constant.CODE_SUCCESS);


    }


    public ResultHelper addCommodity(Commodity commodity) {
        Commodity com = commodityDao.getCommodityByCommodityNo(commodity.getCommodityNo());
        if (com != null) {
            return new ResultHelper(Constant.CODE_ENTITY_DUPLICATED);
        } else {

            Date time = new Date();
            commodity.setCreateTime(time);
            commodity.setUpdateTime(time);
            commodity.setStates(1);
            commodityDao.addCommodity(commodity);
            commodityDao.addCommodityPicture(commodity.getCommodityNo(), commodity.getPictureUrl());
            CommoditySpec commoditySpec = new CommoditySpec();
            commoditySpec.setStates(1);
            commoditySpec.setCommodityNo(commodity.getCommodityNo());
            commodityDao.addCommoditySpec(commoditySpec);
            return new ResultHelper(Constant.CODE_SUCCESS);
        }
    }


    public ResultHelper getCommodityById(int id) {
        Commodity commodity = commodityDao.getCommodityById(id);
        if (commodity != null) {

            if (commodity.getPictureUrl() != null) {
                commodity.setPictureUrl(UrlConstant.QI_NIU_URL + commodity.getPictureUrl());
            }
            List<String> list = commodity.getCommodityPicuture();
            if (list.size() != 0) {
                List<String> newList = new ArrayList<>();
                for (String item : list) {
                    if (item != null) {
                        item = UrlConstant.QI_NIU_URL + item;
                        newList.add(item);
                    }
                }
                commodity.setCommodityPicuture(newList);
            }

            return new ResultHelper(Constant.CODE_SUCCESS, commodity);
        } else {
            return new ResultHelper(Constant.CODE_ENTITY_NOT_FOUND);
        }

    }


    public ResultHelper updateCommodityPicture(String commodityNo, String pictureUrl) {
        commodityDao.addCommodityPicture(commodityNo, pictureUrl);
        return new ResultHelper(Constant.CODE_SUCCESS);
    }


    public ResultHelper deleteCommodityPictrue(String commodityNo) {
        commodityDao.deleteCommodityPicture(commodityNo);
        return new ResultHelper(Constant.CODE_SUCCESS);
    }
}
