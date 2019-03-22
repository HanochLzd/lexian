package com.lexian.manager.shop.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.lexian.manager.shop.bean.Store;
import com.lexian.manager.shop.service.StoreService;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

/**
 * @author Administrator
 */
@Controller
@RequestMapping("store")
@SessionAttributes(value={"isLogining"},types={Boolean.class})
public class StoreController {

	@Autowired
	private StoreService storeService;

	@ResponseBody
	@RequestMapping("getAllStore.do")
	public ResultHelper getAllStore(Page page){
		return storeService.getAllStore(page);
	}
	@ResponseBody
	@RequestMapping("addStore.do")
	public ResultHelper addStore(@Valid Store store){
		return storeService.addStore(store);
	}
	@ResponseBody
	@RequestMapping("updateStore.do")
	public ResultHelper updateStore(@Valid Store store){
		return storeService.updateStore(store);
	}
	@ResponseBody
	@RequestMapping("getStoreByStoreNo.do")
	public ResultHelper getStoreByStoreNo(String storeNo){
		return storeService.getStoreByStoreNo(storeNo);
	}
}
