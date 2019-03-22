package com.lexian.manager.plate.service;


import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

public interface SpecialService {

    ResultHelper getSpecial(Page page);

    ResultHelper updateSpecial(int id, String name);

    ResultHelper deleteSpecial(int id);

    ResultHelper addSpecial(String name);
}
