package com.java1234.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.java1234.model.CarArea;
import com.java1234.model.PageBean;
import com.java1234.service.CarPositionService;
import com.java1234.service.CarAreaService;
import com.java1234.util.PageUtil;
import com.java1234.util.ResponseUtil;
import com.java1234.util.StringUtil;

@Controller
@RequestMapping("/carArea")
public class CarAreaController {

	@Autowired
	private CarAreaService carAreaService;
	
	@Autowired
	private CarPositionService carPositionService;
	
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(value="page",required=false)String page,CarArea carArea,HttpServletRequest request){
		ModelAndView mav=new ModelAndView();
		HttpSession session=request.getSession();
		if(StringUtil.isEmpty(page)){
			page="1";
			session.setAttribute("carArea", carArea);
		}else{
			carArea=(CarArea) session.getAttribute("carArea");
		}
		PageBean pageBean=new PageBean(Integer.parseInt(page),3);
		List<CarArea> carAreaList=carAreaService.find(pageBean, carArea);
		int total=carAreaService.count(carArea);
		String pageCode=PageUtil.getPagation(request.getContextPath()+"/carArea/list.do", total, Integer.parseInt(page), 3);
		mav.addObject("pageCode", pageCode);
		mav.addObject("modeName", "停车区域管理");
		mav.addObject("carAreaList", carAreaList);
		mav.addObject("mainPage", "carArea/list.jsp");
		mav.setViewName("main");
		return mav;
	}
	
	@RequestMapping("/preSave")
	public ModelAndView preSave(@RequestParam(value="id",required=false)String id){
		ModelAndView mav=new ModelAndView();
		mav.addObject("mainPage", "carArea/save.jsp");
		mav.addObject("modeName", "停车区域管理");
		mav.setViewName("main");
		if(StringUtil.isNotEmpty(id)){
			mav.addObject("actionName", "停车区域修改");
			CarArea carArea=carAreaService.loadById(Integer.parseInt(id));
			mav.addObject("carArea", carArea);
		}else{
			mav.addObject("actionName", "停车区域添加");			
		}
		return mav;
	}
	
	@RequestMapping("/save")
	public String save(CarArea carArea){
		if(carArea.getId()==null){
			carAreaService.add(carArea);			
		}else{
			carAreaService.update(carArea);
		}
		return "redirect:/carArea/list.do";
	}
	
	@RequestMapping("/delete")
	public void delete(@RequestParam(value="id")String id,HttpServletResponse response)throws Exception{
		JSONObject result=new JSONObject();
		if(carPositionService.existCarPositionByAreaId(Integer.parseInt(id))){
			result.put("errorInfo", "该停车区域下存在车位，不能删除！");
		}else{
			carAreaService.delete(Integer.parseInt(id));
			result.put("success", true);						
		}
		ResponseUtil.write(result, response);
	}
}
