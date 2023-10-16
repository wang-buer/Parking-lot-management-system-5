package com.java1234.dao;

import java.util.List;

import com.java1234.model.CarType;
import com.java1234.model.PageBean;

public interface CarTypeDao {

	public List<CarType> find(PageBean pageBean,CarType carType);
	
	public int count(CarType carType);
	
	public void add(CarType carType);
	
	public void update(CarType carType);
	
	public void delete(int id);
	
	public CarType loadById(int id);
}
