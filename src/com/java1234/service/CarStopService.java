package com.java1234.service;

import java.util.List;

import javax.servlet.ServletOutputStream;

import com.java1234.model.CarStop;
import com.java1234.model.PageBean;

public interface CarStopService {

	public List<CarStop> find(PageBean pageBean,CarStop carStop);
	
	public int count(CarStop carStop);
	
	public void add(CarStop carStop);
	
	public void update(CarStop carStop);
	
	public void delete(int id);
	
	public CarStop loadById(int id);
	
	public CarStop getFee(int id,String p_endTime);
	
	public void export(String[] titles, ServletOutputStream out);
	
	public boolean existCarStopByCarTypeId(int carTypeId);
	
	public boolean existCarStopByCarPositionId(int carPositionId);
	
	public void updateCarPositionId(CarStop carStop);
}
