package com.java1234.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.java1234.model.CarType;
import com.java1234.model.PageBean;
import com.java1234.service.CarStopService;
import com.java1234.service.CarTypeService;
import com.java1234.util.PageUtil;
import com.java1234.util.ResponseUtil;
import com.java1234.util.StringUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/carType")
public class CarTypeController {

	@Autowired
	private CarTypeService carTypeService;
	
	@Autowired
	private CarStopService carStopService;
	
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(value="page",required=false)String page,CarType carType,HttpServletRequest request){
		ModelAndView mav=new ModelAndView();
		HttpSession session=request.getSession();
		if(StringUtil.isEmpty(page)){
			page="1";
			session.setAttribute("carType", carType);
		}else{
			carType=(CarType) session.getAttribute("carType");
		}
		PageBean pageBean=new PageBean(Integer.parseInt(page),3);
		List<CarType> carTypeList=carTypeService.find(pageBean, carType);
		int total=carTypeService.count(carType);
		String pageCode=PageUtil.getPagation(request.getContextPath()+"/carType/list.do", total, Integer.parseInt(page), 3);
		mav.addObject("pageCode", pageCode);
		mav.addObject("modeName", "车辆类型管理");
		mav.addObject("carTypeList", carTypeList);
		mav.addObject("mainPage", "carType/list.jsp");
		mav.setViewName("main");
		return mav;
	}
	
	@RequestMapping("/preSave")
	public ModelAndView preSave(@RequestParam(value="id",required=false)String id){
		ModelAndView mav=new ModelAndView();
		mav.addObject("mainPage", "carType/save.jsp");
		mav.addObject("modeName", "车辆类型管理");
		mav.setViewName("main");
		if(StringUtil.isNotEmpty(id)){
			mav.addObject("actionName", "车辆类型修改");
			CarType carType=carTypeService.loadById(Integer.parseInt(id));
			mav.addObject("carType", carType);
		}else{
			mav.addObject("actionName", "车辆类型添加");			
		}
		return mav;
	}
	
	@RequestMapping("/save")
	public String save(CarType carType){
		if(carType.getId()==null){
			carTypeService.add(carType);			
		}else{
			carTypeService.update(carType);
		}
		return "redirect:/carType/list.do";
	}
	
	@RequestMapping("/delete")
	public void delete(@RequestParam(value="id")String id,HttpServletResponse response)throws Exception{
		JSONObject result=new JSONObject();
		if(carStopService.existCarStopByCarTypeId(Integer.parseInt(id))){
			result.put("errorInfo", "该车辆类型下存在停车信息，不能删除！");
		}else{
			carTypeService.delete(Integer.parseInt(id));
			result.put("success", true);						
		}
		ResponseUtil.write(result, response);
	}
}
