package com.java1234.dao;

import java.util.List;

import com.java1234.model.CarPosition;
import com.java1234.model.PageBean;

public interface CarPositionDao {

	public List<CarPosition> find(PageBean pageBean,CarPosition carPosition);
	
	public int count(CarPosition carPosition);
	
	public void add(CarPosition carPosition);
	
	public void update(CarPosition carPosition);
	
	public void delete(int id);
	
	public CarPosition loadById(int id);
	
	public boolean existCarPositionByAreaId(int areaId);

	public void updateState(CarPosition carPosition);

	public boolean existCarPositionIsactive(String carPositionNo);
	
	public CarPosition loadByPositionNo(String positionNo);
}
