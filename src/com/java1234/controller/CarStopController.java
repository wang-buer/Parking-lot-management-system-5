package com.java1234.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.java1234.model.CarPosition;
import com.java1234.model.CarStop;
import com.java1234.model.CarType;
import com.java1234.model.PageBean;
import com.java1234.service.CarPositionService;
import com.java1234.service.CarStopService;
import com.java1234.service.CarTypeService;
import com.java1234.util.PageUtil;
import com.java1234.util.ResponseUtil;
import com.java1234.util.StringUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/carStop")
public class CarStopController {

	@Autowired
	private CarStopService carStopService;
	
	@Autowired
	private CarTypeService carTypeService;
	
	@Autowired
	private CarPositionService carPositionService;
	
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(value="page",required=false)String page,CarStop carStop,HttpServletRequest request){
		ModelAndView mav=new ModelAndView();
		HttpSession session=request.getSession();
		if(StringUtil.isEmpty(page)){
			page="1";
			session.setAttribute("carStop", carStop);
		}else{
			carStop=(CarStop) session.getAttribute("carStop");
		}
		PageBean pageBean=new PageBean(Integer.parseInt(page),3);
		List<CarStop> carStopList=carStopService.find(pageBean, carStop);
		int total=carStopService.count(carStop);
		String pageCode=PageUtil.getPagation(request.getContextPath()+"/carStop/list.do", total, Integer.parseInt(page), 3);
		mav.addObject("pageCode", pageCode);
		mav.addObject("modeName", "停车管理");
		mav.addObject("carStopList", carStopList);
		mav.addObject("mainPage", "carStop/list.jsp");
		mav.setViewName("main");
		return mav;
	}
	
	@RequestMapping("/preSave")
	public ModelAndView preSave(@RequestParam(value="id",required=false)String id){
		ModelAndView mav=new ModelAndView();
		mav.addObject("modeName", "停车管理");
		mav.setViewName("main");
		if(StringUtil.isNotEmpty(id)){
			mav.addObject("mainPage", "carStop/update.jsp");
			mav.addObject("actionName", "停车-离场计费");
			CarStop carStop=carStopService.loadById(Integer.parseInt(id));
			mav.addObject("carStop", carStop);
		}else{
			mav.addObject("mainPage", "carStop/save.jsp");
			mav.addObject("actionName", "停车-入场");			
		}
		List<CarType> carTypeList=carTypeService.find(null, null);
		mav.addObject("carTypeList",carTypeList);
		List<CarPosition> carPositionList=carPositionService.find(null, null);
		mav.addObject("carPositionList",carPositionList);
		return mav;
	}
	
	@RequestMapping("/save")
	public String save(CarStop carStop){
		CarPosition carPosition = new CarPosition();
		carPosition.setId(carStop.getCarPositionId());
		if(carStop.getId()==null){
			carStopService.add(carStop);
			carPosition.setIsactive("1");
			carPositionService.updateState(carPosition);
		}else{
			carStopService.update(carStop);
			carPosition.setIsactive("0");
			carPositionService.updateState(carPosition);
		}
		return "redirect:/carStop/list.do";
	}
	
	@RequestMapping("/delete")
	/**
	 * 未离场删除停车信息时，更新停车位状态为空闲
	 * @param id
	 * @param response
	 * @throws Exception
	 */
	public void delete(@RequestParam(value="id")String id,HttpServletResponse response)throws Exception{
		JSONObject result=new JSONObject();
		//修改车位状态为空闲
		CarPosition carPosition = new CarPosition();
		CarStop carStop = carStopService.loadById(Integer.parseInt(id));
		carPosition.setId(carStop.getCarPositionId());
		carPosition.setIsactive("0");
		carStopService.delete(Integer.parseInt(id));
		if(carStop.getStopCost()<=0){
			carPositionService.updateState(carPosition);
		}
		result.put("success", true);						
		ResponseUtil.write(result, response);
	}
	
	@RequestMapping("/getFee")
	public void getFee(@RequestParam(value="id")String id,@RequestParam(value="endTime")String endTime,HttpServletResponse response) throws Exception{
		CarStop carStop = carStopService.getFee(Integer.parseInt(id), endTime);
		JSONObject result=new JSONObject();
		result.put("price", carStop.getPrice());
		result.put("stopDuration", carStop.getStopDuration());
		result.put("stopCost", carStop.getStopCost());
		ResponseUtil.write(result, response);
	}
	
	@RequestMapping("/existCarPositionIsactive")
	public void existCarPositionIsactive(@RequestParam(value="carPositionId")String carPositionId,HttpServletResponse response) throws Exception{
		boolean flag = carPositionService.existCarPositionIsactive(carPositionId);
		JSONObject result=new JSONObject();
		if(flag){
			result.put("success", true);
		}else{
			result.put("errorInfo", "该停车车位编号不再空闲状态中，暂时不能停车！");
		}
		ResponseUtil.write(result, response);
	}
	
	@RequestMapping("/export")
	public void export(HttpServletRequest request, HttpServletResponse response)throws Exception{

		response.setContentType("application/binary;charset=UTF-8");
		try {
			ServletOutputStream out = response.getOutputStream();
			String fileName = new String(
					("离场收费明细" + new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes(), "UTF-8");

			String agent = request.getHeader("USER-AGENT").toLowerCase();
			response.setContentType("application/vnd.ms-excel");
			String codedFileName = java.net.URLEncoder.encode(fileName, "UTF-8");
			if (agent.contains("firefox")) {
				response.setCharacterEncoding("utf-8");
				response.setHeader("content-disposition",
						"attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1") + ".xls");
			} else {
				response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
			}
			
			String[] titles = {"序号","车牌号","车辆类型","停车车位编号","停车开始时间","停车结束时间","每小时收费","停车时长","停车费用"};
			carStopService.export(titles, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
