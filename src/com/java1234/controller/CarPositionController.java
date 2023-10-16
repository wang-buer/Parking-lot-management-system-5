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

import com.java1234.model.CarArea;
import com.java1234.model.CarPosition;
import com.java1234.model.PageBean;
import com.java1234.service.CarAreaService;
import com.java1234.service.CarPositionService;
import com.java1234.service.CarStopService;
import com.java1234.util.PageUtil;
import com.java1234.util.ResponseUtil;
import com.java1234.util.StringUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/carPosition")
public class CarPositionController {

	@Autowired
	private CarPositionService carPositionService;
	
	@Autowired
	private CarAreaService carAreaService;
	
	@Autowired
	private CarStopService carStopService;
	
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(value="page",required=false)String page,CarPosition carPosition,HttpServletRequest request){
		ModelAndView mav=new ModelAndView();
		HttpSession session=request.getSession();
		if(StringUtil.isEmpty(page)){
			page="1";
			session.setAttribute("carPosition", carPosition);
		}else{
			carPosition=(CarPosition) session.getAttribute("carPosition");
		}
		PageBean pageBean=new PageBean(Integer.parseInt(page),3);
		List<CarPosition> carPositionList=carPositionService.find(pageBean, carPosition);
		int total=carPositionService.count(carPosition);
		String pageCode=PageUtil.getPagation(request.getContextPath()+"/carPosition/list.do", total, Integer.parseInt(page), 3);
		mav.addObject("pageCode", pageCode);
		mav.addObject("modeName", "车位管理");
		mav.addObject("carPositionList", carPositionList);
		mav.addObject("mainPage", "carPosition/list.jsp");
		mav.setViewName("main");
		return mav;
	}
	
	@RequestMapping("/preSave")
	public ModelAndView preSave(@RequestParam(value="id",required=false)String id){
		ModelAndView mav=new ModelAndView();
		mav.addObject("mainPage", "carPosition/save.jsp");
		mav.addObject("modeName", "车位管理");
		mav.setViewName("main");
		if(StringUtil.isNotEmpty(id)){
			mav.addObject("actionName", "车位管理修改");
			CarPosition carPosition=carPositionService.loadById(Integer.parseInt(id));
			mav.addObject("carPosition", carPosition);
		}else{
			mav.addObject("actionName", "车位管理添加");			
		}
		List<CarArea> carAreaList=carAreaService.find(null, null);
		mav.addObject("carAreaList",carAreaList);
		return mav;
	}
	
	@RequestMapping("/save")
	public String save(CarPosition carPosition){
		CarArea carArea = carAreaService.loadById(carPosition.getAreaId());
		if(carArea!=null){
			carPosition.setAreaName(carArea.getAreaName());
		}
		if(carPosition.getId()==null){
			carPositionService.add(carPosition);			
		}else{
			carPositionService.update(carPosition);
		}
		return "redirect:/carPosition/list.do";
	}
	
	@RequestMapping("/delete")
	public void delete(@RequestParam(value="id")String id,HttpServletResponse response)throws Exception{
		JSONObject result=new JSONObject();
		if(carStopService.existCarStopByCarPositionId(Integer.parseInt(id))){
			result.put("errorInfo", "该车位下存在停车信息，不能删除！");
		}else{
			carPositionService.delete(Integer.parseInt(id));
			result.put("success", true);						
		}
		ResponseUtil.write(result, response);
	}
}
