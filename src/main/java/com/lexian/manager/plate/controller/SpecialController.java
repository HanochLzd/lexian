package com.lexian.manager.plate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lexian.manager.plate.service.SpecialService;
import com.lexian.web.Page;
import com.lexian.web.ResultHelper;

/**
 * @author luozidong
 */
@Controller
@RequestMapping("special")
public class SpecialController {
	
	@Autowired
	private SpecialService specialService;

	/**
	 * 获取所有的板块id 板块名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getSpecial.do")
	public ResultHelper getSpecial(Page page){
		return specialService.getSpecial(page);
		//special/getSpecial.do?pageNo=1
	}
	/**
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("updateSpecial.do")
	public ResultHelper updateSpecial(int id,String name){
		return specialService.updateSpecial(id, name);
		//special/updateSpecial.do?id=17&name=王子龙
	}
	
	/**
	 * id 是special的id，删除某个板块
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("deleteSpecial.do")
	public ResultHelper deleteSpecial(int id){
		return specialService.deleteSpecial(id);
		//special/deleteSpecial.do?id=6
	}
	/**
	 *添加，首先要判断是否已经存在
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping("addSpecial.do")
	public ResultHelper addSpecial(String name){
		return specialService.addSpecial(name);
		//special/addSpecial.do?name=陈浩
	}
	
}
