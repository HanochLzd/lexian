package com.lexian.manager.plate.service.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lexian.manager.plate.bean.Special;
import com.lexian.manager.plate.dao.SpeCommodityDao;
import com.lexian.manager.plate.dao.SpecialDao;
import com.lexian.manager.plate.service.SpecialService;
import com.lexian.utils.Constant;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

import javax.annotation.Resource;


/**
 * @author Administrator
 */
@Service
public class SpecialServiceImpl implements SpecialService {

    @Resource
    private SpecialDao specialDao;

    @Resource
    private SpeCommodityDao speCommodityDao;

    @Override
    public ResultHelper getSpecial(Page page) {

        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        List<Special> orderssWithStore = specialDao.getSpecialPage(params);
        page.setData(orderssWithStore);

        return new ResultHelper(Constant.CODE_SUCCESS, page);
    }

    @Override
    public ResultHelper updateSpecial(int id, String name) {
        specialDao.updateSpecial(id, name);
        return new ResultHelper(Constant.CODE_SUCCESS);
    }

    @Override
    public ResultHelper deleteSpecial(int id) {
        if (speCommodityDao.getCountSpeCommodities(id) != 0) {
            return new ResultHelper(Constant.CODE_ENTITY_DUPLICATED);
        } else {
            specialDao.deleteSpecial(id);
            return new ResultHelper(Constant.CODE_SUCCESS);
        }
    }

    @Override
    public ResultHelper addSpecial(String name) {
        Special special = specialDao.getSpecialByName(name);
        if (special != null) {
            return new ResultHelper(Constant.CODE_ENTITY_DUPLICATED);
        } else {
            specialDao.addSpecial(name);
            return new ResultHelper(Constant.CODE_SUCCESS);
        }
    }

}
